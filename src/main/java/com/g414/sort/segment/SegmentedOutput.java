package com.g414.sort.segment;

import java.io.IOException;
import java.io.OutputStream;

public interface SegmentedOutput {

	/**
	 * Factory method to use for creating {@link SegmentedInputFile}
	 * for reading contents of this output file back in.
	 */
	SegmentedInput readBack();

	int getSegmentCount();

	OutputStream openNewSegment() throws IOException;

}
