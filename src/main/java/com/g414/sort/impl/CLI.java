package com.g414.sort.impl;

import java.io.*;
import java.util.Comparator;

import com.g414.sort.*;
import com.g414.sort.segment.SegmentedOutputProvider;

/**
 * Simple Command-Line Interface, to work similar to basic Unix
 * sort (although with way fewer options of course)
 */
public class CLI
{
    public CLI() { }
    
    public static void main(String[] args) throws Exception
    {
        new CLI().sort(args);
        System.err.println("Done.");
    }

    private final String ENCODING = "ISO-8859-1";
    
    public void sort(String[] args) throws Exception
    {
        if (args.length != 1) {
            System.err.println("Usage: java "+getClass().getName()+" [input-file]");
            System.exit(1);
        }
        File input = new File(args[0]);
        if (!input.exists() || input.isDirectory()) {
            System.err.println("File '"+input.getAbsolutePath()+"' does not exist (or is not file)");
            System.exit(2);
        }
        
        // just create standard instance with default configs
        StandardSortConfig config = new StandardSortConfig();
        // except for using 200 megs for in-memory pre-sorting
        config = config.maxMemoryUsage(200 * 1000 * 1000);
        ExternalSort sorter = new ExternalSort(config);        
        sorter.readAndSort(
                (SortableDataSource<String>)new LineBasedDataSource(input, ENCODING),
                (SortableDataSourceProvider<String>)new LineBasedDataSourceProvider(ENCODING),
                (SortableDataWriter<String>)new ToStringDataWriter<String>(ENCODING),
                (Comparator<String>)new StringComparator(),
                new TempFileSegmentedOutputProvider(config, "sorta-"),
                System.out
                );
        /*
        public <T> boolean readAndSort(SortableDataSource< ? extends T > sdsIn,
                SortableDataSourceProvider< ? extends T > sdspTemp, SortableDataWriter< ? super T > sdwTemp,
                Comparator< ? super T > cmpT,
                SegmentedOutputProvider sopTemps,
                OutputStream ostFinal)
                */
    }

    private final static class StringComparator implements Comparator<String>
    {
        @Override
        public int compare(String arg0, String arg1) {
            return arg0.compareTo(arg1);
        }
    }
}
