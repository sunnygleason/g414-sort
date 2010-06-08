package com.g414.sort.impl;

import com.g414.sort.api.*;

public class StandardSorter
    implements Sorter
{
    /*
    /************************************************************************
    /* Configuration
    /************************************************************************
     */

    protected final SortConfig<?> _config;

    /*
    /************************************************************************
    /* State, cancellation
    /************************************************************************
     */
    
    protected volatile boolean _cancellationRequested = false;

    protected volatile RuntimeException _cancelUsingException;

    /*
    /************************************************************************
    /* Construction
    /************************************************************************
     */
    
    public StandardSorter(SortConfig<?> config) {
        _config = config;
    }

    public SortConfig<?> getConfig() { return _config; }
    
    /*
    /************************************************************************
    /* SortState, cancellation
    /************************************************************************
     */
    
    @Override
    public void cancel(RuntimeException e) {
        _cancelUsingException = e;
        _cancellationRequested = true;
    }

    /*
    /************************************************************************
    /* SortState, Accessors
    /************************************************************************
     */

    @Override
    public int getNumberOfSortRounds() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSortRound() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isCompleted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPreSorting() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSorting() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
    /************************************************************************
    /* Helper methods
    /************************************************************************
     */

    protected void _handleCancel()
    {
        RuntimeException e = _cancelUsingException;
        if (e != null) {
            throw e;
        }
        return;
    }
}
