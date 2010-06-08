package com.g414.sort.api;

/**
 * Interface for basic sorting configuration.
 * Used via "fluent" pattern so that each configuration instance is fully immutable
 * and new instances are constructed by methods; methods are usually chained for
 * convenience.
 * 
 * @author tatu
 *
 * @param <T> Type parameter we need to use to allow for proper chaining and result
 *    types with sub-classing
 */
public abstract class SortConfig<T extends SortConfig<T>>
{
    /*
    /************************************************************************
    /* Accessors
    /************************************************************************
     */

    public abstract long maxmMemoryUsage();

    public abstract TempFileProvider tempFileProvider();
    
    /*
    /************************************************************************
    /* Fluent construction methods
    /************************************************************************
     */
    
    /**
     * Method for constructing configuration instance that defines that maximum amount
     * of memory to use for pre-sorting. This is generally a crude approximation and
     * implementations make best effort to honor it.
     * 
     * @param maxMem Maximum memory that pre-sorted should use for in-memory sorting
     * @return New 
     */
    public abstract T maxMemoryUsage(long maxMem);

    public abstract T tempFileProvider(TempFileProvider provider);
}
