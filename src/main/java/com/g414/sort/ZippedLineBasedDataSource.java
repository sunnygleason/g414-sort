package com.g414.sort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Decorator data source that can be used to handle zipped line-based
 * data.
 */
public class ZippedLineBasedDataSource implements SortableDataSource<String>
{
    protected final LineBasedDataSource source;

    protected final String entryName;
    
    public ZippedLineBasedDataSource(File f) throws IOException {
        this(f, "UTF-8");
    }

    public ZippedLineBasedDataSource(File f, String enc) throws IOException {
        this(new FileInputStream(f), enc);
    }

    public ZippedLineBasedDataSource(InputStream in, String enc) throws IOException {
        ZipInputStream zin = new ZipInputStream(in);
        //zip file should only contain one entry
        ZipEntry entry = zin.getNextEntry();
        if (entry == null) {
            throw new IOException("Empty zip input stream, no entries found");
        }
        entryName = entry.getName();
        source = new LineBasedDataSource(zin, enc);
    }

    public String next() throws IOException {
        return source.next();
    }

    public String getEntryName() { return entryName; }
}