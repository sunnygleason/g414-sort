package com.g414.sort.impl;

import com.g414.sort.api.*;

public class StandardSortConfig
    extends SortConfig<StandardSortConfig>
{
    /**
     * By default let's use 10 megs for pre-sorting
     */
    public final static long DEFAULT_MAX_MEMORY = 10000000L;
    
    protected final long _maxMemory;

    protected final TempFileProvider _tempFileProvider;
    
    public StandardSortConfig() {
        this(DEFAULT_MAX_MEMORY, new StandardTempFileProvider());
    }

    public StandardSortConfig(long maxMemory, TempFileProvider tfp) {
        _maxMemory = maxMemory;
        _tempFileProvider = tfp;
    }

    /*
    /************************************************************************
    /* Accessors
    /************************************************************************
     */
    
    @Override
    public long maxmMemoryUsage() { return _maxMemory; }

    @Override
    public TempFileProvider tempFileProvider() { return _tempFileProvider; }
    
    /*
    /************************************************************************
    /* Fluent construction methods
    /************************************************************************
     */

    @Override
    public StandardSortConfig maxMemoryUsage(long maxMem) {
        return new StandardSortConfig(maxMem, _tempFileProvider);
    }

    @Override
    public StandardSortConfig tempFileProvider(TempFileProvider provider) {
        return new StandardSortConfig(_maxMemory, provider);
    }
}
