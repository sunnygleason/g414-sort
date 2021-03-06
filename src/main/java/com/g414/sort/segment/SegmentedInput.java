package com.g414.sort.segment;

import java.io.IOException;
import java.io.InputStream;

public interface SegmentedInput {

	InputStream openNext() throws IOException;

	long length();

	String getIdentifier();

	int getSegmentCount();

	int getCurrentSegmentIndex();

}
