package com.g414.sort;

import java.io.IOException;
import java.io.OutputStream;

public interface SortableDataWriter< T > {
	void startSegment(OutputStream out) throws IOException;
	void finishSegment() throws IOException;
	void writeEntry(T entry) throws IOException;
	void writeEntries(T[] entries, int start, int len) throws IOException;
}
