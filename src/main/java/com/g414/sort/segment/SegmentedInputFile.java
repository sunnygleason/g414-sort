package com.g414.sort.segment;

import java.io.*;

public class SegmentedInputFile implements SegmentedInput
{
    final File mFile;

    final long mTotalLength;

    final long[] mSegmentOffsets;
    final boolean mDisposeAtEnd;

    int mSegmentIndex = 0;

    protected SegmentedInputFile(File f, long[] segOffsets, boolean flgDisposeAtEnd)
    {
        mFile = f;
        mTotalLength = f.length();
        mSegmentOffsets = segOffsets;
        mDisposeAtEnd = flgDisposeAtEnd;
    }

    public InputStream openNext()
        throws IOException
    {
        if (mSegmentIndex >= mSegmentOffsets.length) {
            return null;
        }
        long currOffset = mSegmentOffsets[mSegmentIndex];
        ++mSegmentIndex;
        boolean flgDispose = mSegmentIndex == mSegmentOffsets.length;
        long nextOffset = flgDispose ? mTotalLength : mSegmentOffsets[mSegmentIndex];
        return new SegmentedInputStream(mFile, currOffset, nextOffset - currOffset, flgDispose && mDisposeAtEnd );
    }

    public long length()
    {
        return mFile.length();
    }

    public String getIdentifier() { return mFile.getAbsolutePath(); }

    public int getSegmentCount() { return mSegmentOffsets.length; }

    public int getCurrentSegmentIndex() { return mSegmentIndex; }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(100);
        sb.append("[Segmented input file '");
        sb.append(mFile.getAbsolutePath());
        sb.append("', ").append(length());
        int segs = getSegmentCount();
        sb.append(" bytes, ").append(segs);
        sb.append(" segments; offsets (");
        for (int i = 0; i < segs; ++i) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(mSegmentOffsets[i]);
        }
        sb.append(")");
        return sb.toString();
    }

    /*
    ///////////////////////////////////////////////////////////////
    // Helper classes
    ///////////////////////////////////////////////////////////////
     */

    /**
     * InputStream that will read a limited section of given input
     * file
     */
    final static class SegmentedInputStream
        extends InputStream
    {
        final RandomAccessFile mRandomAccess;
        final File mFile;
        final long mEnd;
        final boolean mDispose;

        public SegmentedInputStream(File f, long offset, long len, boolean flgDisposeAtEnd)
            throws IOException
        {
            mFile = f;
            mRandomAccess = new RandomAccessFile(f, "r");
            mRandomAccess.seek(offset);
            mEnd = offset+len;
            mDispose = flgDisposeAtEnd;
        }

        public int available() {
            // We really don't know, do we?
            return -1;
        }

        public void close()
            throws IOException
        {
            mRandomAccess.close();
            if ( mDispose )
                mFile.delete();
        }

        public void mark(int readlimit) {
            throw new UnsupportedOperationException();
        }

        public boolean markSupported() { return false; }

        public int read()
            throws IOException
        {
            if (mRandomAccess.getFilePointer() >= mEnd) {
                close();
                return -1;
            }
            return mRandomAccess.read();
        }

        public int read(byte[] b)
            throws IOException
        {
            return read(b, 0, b.length);
        }

        public int read(byte[] b, int offset, int len)
            throws IOException
        {
            if (len < 1) { // sanity check
                return len;
            }
            long currPos = mRandomAccess.getFilePointer();
            if ((currPos + len) > mEnd) { // can only read some
                // EOF?
                long remaining = mEnd - currPos;
                if (remaining <= 0L) {
                    close();
                    return -1;
                }
                len = ( int )remaining;
            }
            return mRandomAccess.read(b, offset, len);
        }

        public void reset() {
            throw new UnsupportedOperationException();
        }

        public long skip(long n)
            throws IOException
        {
            long currPos = mRandomAccess.getFilePointer();
            long newPos = currPos + n;

            if (newPos > mEnd) {
                newPos = mEnd;
            }

            mRandomAccess.seek(newPos);

            return (newPos - currPos);
        }
    }
}
