package com.g414.sort.api;

/**
 * Interface that defines how calling application can interact with a {@link Sorter}; both
 * by accessing progress information and by requesting cancellation if necessary.
 * 
 * @author tatu
 */
public interface SortState
{
    public final static int ROUND_NOT_STARTED = -1;

    public final static int ROUND_PRE_SORTING = 0;
    
    public final static int ROUND_COMPLETED = -2;

    /*
    /************************************************************************
    /* Accessors
    /************************************************************************
     */
    
    /**
     * Accessor for determining whether sorter is in its in-memory pre-sorting phase.
     */
    public boolean isPreSorting();
    
    /**
     * Accessor for determining whether sorter is in regular merge-sort phase or not.
     */
    public boolean isSorting();

    /**
     * Accessor for determining whether sorting has been succesfully completed or not.
     */
    public boolean isCompleted();

    /**
     * Accessor for checking which sorting round sorted is doing; regular sorting rounds
     * have non-zero positive values (1, 2, ...) and special constants
     * {@link #ROUND_NOT_STARTED}, {@link #ROUND_PRE_SORTING} and {@link #ROUND_COMPLETED}
     * are used for other states.
     */
    public int getSortRound();

    /**
     * Accessor for figuring out how many regular sorting rounds need to be taken to
     * complete sorting, if known. If information is not known, will return -1.
     * This information generally becomes available after pre-sorting round.
     */
    public int getNumberOfSortRounds();
    
    /*
    /************************************************************************
    /* Cancellation
    /************************************************************************
     */

    /**
     * Method that can be used to try to cancel executing sort operation.
     * Optional exception object can be specified; if non-null instance is given,
     * it will be thrown to indicate erroneous result, otherwise sorting is
     * just interrupted but execution returns normally.
     */
    public void cancel(RuntimeException e);
}
