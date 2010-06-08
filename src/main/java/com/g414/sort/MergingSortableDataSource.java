package com.g414.sort;

import java.io.IOException;
import java.util.Comparator;

/**
 * Specialized data source which can read from two sources and
 * merge them assuming both are individually sorted.
 * This is used to implement merge sort.
 */
public abstract class MergingSortableDataSource<T>
    implements SortableDataSource<T>
{
    protected MergingSortableDataSource() { }

    public static <T> MergingSortableDataSource<T> create(SortableDataSource<? extends T> source1,
            SortableDataSource<? extends T> source2, Comparator<? super T> cmpT)
        throws IOException
    {
        return new ComparatorBased<T>(source1, source2, cmpT);
    }

    
    public static <T extends Comparable<T>> MergingSortableDataSource<T> create(SortableDataSource< ? extends T > source1,
            SortableDataSource< ? extends T > source2)
        throws IOException
    {
        return new ComparableBased<T>(source1, source2);
    }
    
    public abstract T next() throws IOException;

    /*
    /******************************************************************************
    /* Concrete sub-classes
    /******************************************************************************
     */

    public static class ComparatorBased<T>
        extends MergingSortableDataSource<T>
    {
        protected final SortableDataSource< ? extends T > source1, source2;
        protected final Comparator< ? super T > cmpT;

        protected T currentData1, currentData2;
        
        public ComparatorBased(SortableDataSource<? extends T> source1,
                SortableDataSource<? extends T> source2, Comparator< ? super T > cmpT)
            throws IOException
        {
            super();
            this.source1 = source1;
            this.source2 = source2;
            this.cmpT = cmpT;
        
            currentData1 = (source1 == null) ? null : source1.next();
            currentData2 = (source2 == null) ? null : source2.next();
        }

        public T next() throws IOException
        {
            T result;

            // Data is null if matching source is closed (or missing)
            if (currentData1 == null) {
                result = currentData2;
                if (result != null) { // can both be null
                    currentData2 = source2.next();
                }
            } else if (currentData2 == null) {
                result = currentData1;
                currentData1 = source1.next();
            } else { // data for both, need to compare
                /* We will output mData1 stuff first if equal, to keep
                 * sorting "stable" (i.e. minimize order changes), assuming
                 * that input 'data 1' comes before 'data 2' all other things
                 * being equal.
                 */
                if ( cmpT.compare( currentData1, currentData2 ) <= 0 ) {
                    result = currentData1;
                    currentData1 = source1.next();
                } else {
                    result = currentData2;
                    currentData2 = source2.next();
                }
            }
            return result;
        }    
    }

    public static class ComparableBased<T extends Comparable<T>>
        extends MergingSortableDataSource<T>
    {
        protected final SortableDataSource< ? extends T > source1, source2;

        protected T currentData1, currentData2;

        public ComparableBased(SortableDataSource<? extends T> source1, SortableDataSource<? extends T> source2)
            throws IOException
        {
            super();
            this.source1 = source1;
            this.source2 = source2;
            currentData1 = (source1 == null) ? null : source1.next();
            currentData2 = (source2 == null) ? null : source2.next();
        }

        public T next() throws IOException
        {
            T result;

            // Data is null if matching source is closed (or missing)
            if (currentData1 == null) {
                result = currentData2;
                if (result != null) { // both can not be null
                    currentData2 = source2.next();
                }
            } else if (currentData2 == null) {
                result = currentData1;
                currentData1 = source1.next();
            } else { // data for both, need to compare
                /* We will output mData1 stuff first if equal, to keep
                 * sorting "stable" (i.e. minimize order changes), assuming
                 * that input 'data 1' comes before 'data 2' all other things
                 * being equal.
                 */
                if (currentData1.compareTo(currentData2 ) <= 0 ) {
                    result = currentData1;
                    currentData1 = source1.next();
                } else {
                    result = currentData2;
                    currentData2 = source2.next();
                }
            }
            return result;
        }

    
    }
}
