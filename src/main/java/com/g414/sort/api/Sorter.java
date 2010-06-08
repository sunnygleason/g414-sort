package com.g414.sort.api;

/**
 * Main abstraction for objects that handle sorting at high level.
 * Implements {@link SortState} which defines interface for querying progress status
 * as well as requesting cancellation if necessary.
 * 
 * @author tatu
 */
public interface Sorter
    extends SortState
{
    public SortConfig<?> getConfig();
}
