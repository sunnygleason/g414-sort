package com.g414.sort;

import java.io.IOException;

public interface SortableDataSource< T > {
	T next() throws IOException;
}
