package com.g414.sort;

public interface SortableDataWriterProvider< T > {
	SortableDataWriter< T > createDataWriter();
}
