package com.g414.sort;

import java.io.IOException;
import java.io.OutputStream;

public class SingletonSegmentedOutputStream implements SegmentedOutput {
	private final OutputStream ostDelegate;
	private boolean flgOpened = false;
	public SingletonSegmentedOutputStream( OutputStream ostDelegate ) {
		this.ostDelegate = ostDelegate;
	}

	public synchronized OutputStream openNewSegment() throws IOException {
		if ( flgOpened )
			return null;
		flgOpened = true;
		return ostDelegate;
	}

	public int getSegmentCount() {
		return flgOpened ? 0 : 1;
	}

	public SegmentedInput readBack() {
		// can't really return an input stream for a generic output stream
		return null;
	}

}
