package com.g414.sort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Trivial implementation for reading file lines for sorting purposes
 */
public class LineBasedDataSource implements SortableDataSource<String>
{
    protected final BufferedReader reader;

    public LineBasedDataSource(File f) throws IOException {
        this(f, "UTF-8");
    }

    public LineBasedDataSource(File f, String enc) throws IOException
    {
        this(new FileInputStream(f), enc);
    }

    public LineBasedDataSource(InputStream in, String enc) throws IOException
    {
        reader = new BufferedReader(new InputStreamReader(in, enc));
    }
    
    public String next() throws IOException
    {
        String line = reader.readLine();
        if (line == null) {
            reader.close();
        }
        return line;
    }
}