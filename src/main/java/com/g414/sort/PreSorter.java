package com.g414.sort;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import com.g414.sort.api.SortConfig;
import com.g414.sort.segment.SegmentedInput;
import com.g414.sort.segment.SegmentedOutput;

/**
 * Helper class that manages first part of generic sorting process:
 * that of buffering and sorting input in memory into bounded
 * chunks. This makes actual merge sort more efficient.
 *<p>
 * Main configuration is by specifying (fixed) number of entries
 * to buffer and sort. Higher this is set, faster merge sort will
 * be, but memory usage will also be higher.
 */
public class PreSorter< T >
{
    // // // Configuration

    protected final Comparator< ? super T > cmpT;
    protected final SortableDataWriter< ? super T > mWriter;
    protected SegmentedOutput mOutput;

    // // // Buffering

    protected final Object[] mEntryBuffer;

    protected int mEntryCount;

    @SuppressWarnings( "unchecked" )
    public PreSorter(SegmentedOutput output, SortConfig config,
        SortableDataWriter< ? super T > writer, Comparator< ? super T > cmpT
    ) {
        mWriter = writer;
        /* @TODO: This is not quite right; there is no strict relationship
         * between entries, mem usage. Must fix.
         * 
         * For now, assume mem == entries * 16
         */
        int entriesToBuffer = (int) (config.maxmMemoryUsage() >> 4) + 1;
        this.cmpT = cmpT;
        mEntryBuffer = new Object[entriesToBuffer];
        mEntryCount = 0;

        mOutput = output;
    }

    public void addEntry(T entry)
        throws IOException
    {
        mEntryBuffer[mEntryCount] = entry;
        if (++mEntryCount >= mEntryBuffer.length) {
            flushBuffer();
        }
    }

    public SegmentedInput close()
        throws IOException
    {
        flushBuffer();
        return mOutput.readBack();
    }

    @SuppressWarnings("unchecked")
    protected void flushBuffer()
        throws IOException
    {
        int count = mEntryCount;
        if (mEntryCount < 1) {
            return;
        }
        mEntryCount = 0;

        // First: must sort them
        Arrays.sort((T[]) mEntryBuffer, 0, count, cmpT );

        // Then open segment, write stuff, close segment
        mWriter.startSegment(mOutput.openNewSegment());
        try {
            mWriter.writeEntries((T[])mEntryBuffer, 0, count);
        } finally {
            mWriter.finishSegment();
        }
    }
}

