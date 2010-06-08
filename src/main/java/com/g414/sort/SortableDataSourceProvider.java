package com.g414.sort;

import java.io.IOException;
import java.io.InputStream;

public interface SortableDataSourceProvider< T > {
	/**
	 * Creates a new Sortable Data Source from an input stream.
	 * @param in the input stream; may be null!
	 * @return a new data source, or null if the input stream is null
	 * @throws IOException
	 **/
	public SortableDataSource< T > createDataSource( InputStream in ) throws IOException;
}
