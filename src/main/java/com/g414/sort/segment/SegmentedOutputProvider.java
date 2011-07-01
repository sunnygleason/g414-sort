package com.g414.sort.segment;

import java.io.IOException;

public interface SegmentedOutputProvider {
	SegmentedOutput create( String strPrefix ) throws IOException;
}
