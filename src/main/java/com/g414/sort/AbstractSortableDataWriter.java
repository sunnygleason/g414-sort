package com.g414.sort;

import java.io.IOException;

/**
 * Includes a helper to reduce the size of the SortableDataWriter interface
 * @author tatus
 * @author jcaplan
 *
 */
public abstract class AbstractSortableDataWriter< T > implements SortableDataWriter< T > {
	public void writeEntries( T[] entries, int start, int len )
	throws IOException {
		len += start;

		for ( ; start < len; ++start )
			writeEntry( entries[ start ] );
	}
}
