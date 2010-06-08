package com.g414.sort;

import java.io.*;

import com.g414.sort.AbstractSortableDataWriter;

/**
 * Simple {@link SortableDataWriter} implementation that just calls
 * <code>toString()</code> on entries to produce output serialization
 */
public class ToStringDataWriter< T > extends AbstractSortableDataWriter< T >
{
    /**
     * Better buffer output using a reasonable output buffer size.
     * 64k seems reasonable for now.
     */
    final static int BUFF_SIZE = 64*1024;

    protected final String mEncoding;
    
    protected Writer mWriter;

    public ToStringDataWriter() {
        // default to UTF-8
        this("UTF-8");
    }

    public ToStringDataWriter(String enc) {
        mEncoding = enc;
    }
    
    public void finishSegment() throws IOException {
        mWriter.close();
    }

    public void startSegment(OutputStream out) throws IOException {
        mWriter = new BufferedWriter(new OutputStreamWriter(out, mEncoding), BUFF_SIZE);
    }

    public void writeEntry(T entry) throws IOException {
        mWriter.write(entry.toString());
        mWriter.write('\n');
    }
}
