package com.g414.sort;

import java.io.IOException;

public interface SegmentedOutputProvider {
	SegmentedOutput create( String strPrefix ) throws IOException;
}
