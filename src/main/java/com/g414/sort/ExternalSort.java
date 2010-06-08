package com.g414.sort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;

import com.g414.sort.api.Sorter;
import com.g414.sort.api.SortConfig;
import com.g414.sort.impl.StandardSortConfig;
import com.g414.sort.impl.StandardSorter;

/**
 * Collection of helper methods to use for pre-sorting data files.
 *<p>
 * Note: might be a good candidate to include in a commons package,
 * since there is nothing particularly specific to notability
 * handling.
 * 
 * @author tatu
 */
public class ExternalSort
    extends StandardSorter
    implements Sorter
{
        public ExternalSort() {
            this(new StandardSortConfig());
        }
	
	public ExternalSort(SortConfig<?> config) {
	    super(config);
	}

	/*
	 ****************************************************************
	 * Methods for pre-sorting data
	 ****************************************************************
	 */

	/**
	 * Method that handles first part of sorting process: pre-sorting
	 * of entries to speed up actual disk-based merge sort
	 * @param parentTask Task that needs data to be sorted; may be null.
	 *    If not null, progress indicators will be updated during pre-sorting
	 * 
	 * @return Resulting file to use for full sorting, if all goes well; null
	 *    if pre-sorting was cancelled by parent task.
	 **/
	public < T > SegmentedInput presort(SortableDataSource< ? extends T > src, SortableDataWriter< ? super T > dstWriter, Comparator< ? super T > cmpT, SegmentedOutput output)
	throws IOException
	{
		//context data is huge, so use batches of 8M for presort
		PreSorter< T > presorter = new PreSorter< T >(output, _config, dstWriter, cmpT);
		T entry;

		while ((entry = src.next()) != null) {
			//tskParent.addReadCount();
			if (this._cancellationRequested) {
			    _handleCancel();
			    return null;
			}
			presorter.addEntry(entry);
		}
		SegmentedInput result = presorter.close();
		/*
		tskParent.appendProcessingDesc("Completed pre-sorting: read %s entries, wrote %d segments, file: %s bytes",
			NumberUtil.getCountDesc(tskParent.getReadCount()),
			result.getSegmentCount(),
			NumberUtil.getCountDesc(result.length())
		);
                        */
		return result;
	}

	/**
	 * Method called to merge segments (count >= 3) by merge sorting
	 * pairs of segments thus halving number of segments.
	 */
	public < T > SegmentedInput mergeNSegments(
		SegmentedInput presorted, SortableDataSourceProvider< ? extends T > sources, SortableDataWriter< ? super T > resultWriter,
		Comparator< ? super T > cmpT, SegmentedOutput segOut
	) throws IOException {
		SortableDataSource< ? extends T > src1;
		while ( null != ( src1 = sources.createDataSource( presorted.openNext() ) ) ) {
			SortableDataSource< ? extends T > src2 = sources.createDataSource( presorted.openNext() );
			// (that may be null, for the last 'odd' segment
			OutputStream out = segOut.openNewSegment();
			try {
				resultWriter.startSegment(out);
				this.< T >merge2Segments(src1, src2, resultWriter, cmpT);
				resultWriter.finishSegment();
			} finally {
				out.close();
			}
			//tskParent.addWriteCount(2);
		}

		return segOut.readBack();
	}

	/**
	 * Method called to specifically merge 2 segments using merge
	 * sort, into a single result segment; or to just copy a single
	 * segment as is. Latter is needed for "odd" segments, including
	 * cases where the input consists of just a single segment.
	 */
	public < T > void merge2Segments(
		SortableDataSource< ? extends T > src1, SortableDataSource< ? extends T > src2, SortableDataWriter< ? super T > dataWriter,
		Comparator< ? super T > cmpT
	) throws IOException {
		SortableDataSource< ? extends T > src;

		if (src2 == null) {
			src = src1;
		} else {
			src = MergingSortableDataSource.< T >create(src1, src2, cmpT);
		}

		T d;
		while ((d = src.next()) != null) {
			dataWriter.writeEntry(d);
			//tskParent.addReadCount();
		}
	}

	/*
	 ****************************************************************
	 * Methods for sorting pre-sorting sorted data
	 ****************************************************************
	 */

	public < T > boolean readAndSort(SortableDataSource< ? extends T > sdsIn,
		SortableDataSourceProvider< ? extends T > sdspTemp, SortableDataWriter< ? super T > sdwTemp,
		Comparator< ? super T > cmpT,
		SegmentedOutputProvider sopTemps,
		OutputStream ostFinal)
	    throws IOException
	{
		//tskParent.resetCounts();

		// First: let's read and partially sort data
		// "this.< T >" necessary to satisfy idiotic compiler (not needed in Eclipse JDT)
		SegmentedInput presorted = this.< T >presort( sdsIn, sdwTemp, cmpT, sopTemps.create( "pre-sorted-" ) );
		if ( presorted == null ) { // cancel
			return false;
		}

		//tskParent.appendProcessingDesc( "Completed reading, pre-sorting marketplace %d", marketplaceId );
		//tskParent.setStatus("Sorting segments");
		return this.< T >sort( sdspTemp, sdwTemp, cmpT, presorted, sopTemps, ostFinal );
	}

	public < T > boolean sort(SortableDataSourceProvider< ? extends T > sources, SortableDataWriter< ? super T > target,
		Comparator< ? super T > cmpT,
		SegmentedInput presorted, SegmentedOutputProvider sopTemps,
		OutputStream ostFinal) throws IOException
	{
		int round = 0;
		boolean lastMerge = false;

		while (!lastMerge) {
			// Last merge/copy differs from intermediate ones
			lastMerge = (presorted.getSegmentCount() <= 2);
			SegmentedOutput segOut;

                        if (this._cancellationRequested) {
                            _handleCancel();
                            return false;
                        }

                        if (lastMerge) {
                            /*
				tskParent.setStatus("Final sort/copy");
				tskParent.appendProcessingDesc("Starting sorting round #%d (last)", round );
				*/
				segOut = new SingletonSegmentedOutputStream( ostFinal );
			} else {
				//tskParent.appendProcessingDesc("Starting sorting round #%d (intermediate)", round ); 
				segOut = sopTemps.create( "semi-sorted-" );
			}
			SegmentedInput presorted2 = this.< T >mergeNSegments(presorted, sources, target, cmpT, segOut);
			if (!lastMerge && presorted2 == null) {
			    return false;
			}
			//tskParent.appendProcessingDesc("Sorting round #%d completed, will delete source file '%s;", round, presorted.getIdentifier());
			presorted = presorted2;
			++round;
		}
		//tskParent.setStatus("Sorting completed");
		return true;
	}
}
