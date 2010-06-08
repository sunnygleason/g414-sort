package com.g414.sort;

import java.io.*;

import com.g414.sort.api.SortConfig;

/**
 * Collection of helper methods to use for pre-sorting data files.
 *<p>
 * Note: might be a good candidate to include in a commons package,
 * since there is nothing particularly specific to notability
 * handling.
 * 
 * @author tatus
 */
public class ZippedTextFileSort
{
    final ExternalSort _sorter;

    public ZippedTextFileSort() {
        this(null);
    }

    public ZippedTextFileSort(SortConfig<?> config) {
        _sorter = new ExternalSort(config);
    }

    /*
     ****************************************************************
     * Methods for sorting pre-sorted sorted data
     ****************************************************************
     */

    public boolean sortZippedTextFile(File dwFile, String outFileBasename, int marketplaceId, File outFile)
        throws IOException
    {
        return sortZippedTextFile( dwFile, outFileBasename, "ASIN", marketplaceId, outFile );
    }
    /**
     * Method for sorting simple textual data, where sorting can be done by just sorting
     * line contents without any parsing beyond character decoding.
     */
    public boolean sortZippedTextFile(File dwFile, String outFileBasename, String strHeaderMarker, int marketplaceId, File outFile)
        throws IOException
    {
        // original input comes as zipped text file
        ZippedLineBasedDataSource src = new ZippedLineBasedDataSource(dwFile, "UTF-8");
        // and has a single header line to skip:
        String header = src.next();
        if (!header.startsWith(strHeaderMarker)) {
            if (header.length() > 40) {
                header = header.substring(0, 40)+"[...]";
            }
            throw new IOException("File '"+dwFile.getAbsolutePath()+"' (zip entry '"+src.getEntryName()+"') does not start with header line: should start with '" + strHeaderMarker + "', is: '"+header+"'");
        }
         
        return _sorter.< String >readAndSort(
            src,
            new LineBasedDataSourceProvider("UTF-8"),
            new ToStringDataWriter<String>(),
            null, // use natural ordering on Strings
            new TempFileSegmentedOutputProvider(_sorter.getConfig(), outFileBasename),
            new FileOutputStream( outFile )
        );
    }

}
