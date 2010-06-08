package com.g414.sort;

import java.io.*;
import java.util.*;

public class SegmentedOutputFile implements SegmentedOutput
{
    protected final File mFile;

    /**
     * Sequence of offsets in physical file that define actual
     * logical segments to read.
     */
    protected ArrayList<Long> mOffsets = new ArrayList<Long>();

    public SegmentedOutputFile(File f)
    {
        mFile = f;
    }

    /**
     * Factory method to use for creating {@link SegmentedInputFile}
     * for reading contents of this output file back in.
     */
    public SegmentedInput readBack()
    {
        int len = mOffsets.size();
        long[] offsets = new long[len];

        for (int i = 0; i < len; ++i) {
            offsets[i] = mOffsets.get(i).longValue();
        }
        return new SegmentedInputFile(mFile, offsets, true);
    }

//    public File getUnderlyingFile() { return mFile; }

    public long length() throws IOException
    {
        return mFile.length();
    }

    public int getSegmentCount() { return mOffsets.size(); }

    public OutputStream openNewSegment()
        throws IOException
    {
        OutputStream result;
        long offset;

        if (mOffsets.isEmpty()) { // first one -- remove existing if any
            offset = 0L;
            result = new FileOutputStream(mFile, false);
        } else {
            offset = mFile.length();
            result = new FileOutputStream(mFile, true);
            /* Minor sanity check to (try to?) avoid problems if
             * file gets truncated etc
             */
            Long prev = mOffsets.get(mOffsets.size()-1);
            if (prev.longValue() >= offset) {
                if (prev.longValue() > offset) {
                    throw new IOException("Error when trying to open new segment: previous offset "+prev.longValue()+" (segments: "+mOffsets.size()+"), new offset "+offset+" (file '"+mFile.getAbsolutePath()+"')");
                }
                // But how about empty segments? Not good, for now
                throw new IOException("Error when trying to open new segment: empty segment at "+mOffsets.size()+" (file '"+mFile.getAbsolutePath()+"')");
            }
        }
        mOffsets.add(Long.valueOf(offset));
        return result;
    }
}
