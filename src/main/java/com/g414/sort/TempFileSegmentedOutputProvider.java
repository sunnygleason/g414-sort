package com.g414.sort;

import java.io.IOException;

import com.g414.sort.api.SortConfig;
import com.g414.sort.segment.SegmentedOutput;
import com.g414.sort.segment.SegmentedOutputFile;
import com.g414.sort.segment.SegmentedOutputProvider;

public class TempFileSegmentedOutputProvider implements SegmentedOutputProvider
{
    private final SortConfig<?> _config;

    //private final ResourceManagingTask tskParent;
    private final String _basename;

    public TempFileSegmentedOutputProvider(SortConfig<?> config, String basename)
    {
        _config = config;
        //this.tskParent = tskParent;
        _basename = basename;
    }

    public SegmentedOutput create(String strPrefix) throws IOException
    {
        return new SegmentedOutputFile(_config.tempFileProvider().provide(strPrefix + _basename, ".tmp"));
    }
}
