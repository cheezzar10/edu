package com.odin.rnd.edu.gcactivity;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ByteBlockAllocator {
	private final AtomicInteger hash = new AtomicInteger();
	private final int blockSize;
	private final AtomicBoolean stopped = new AtomicBoolean();
	
	public ByteBlockAllocator(int blkSz) {
		blockSize = blkSz;
	}
	
	private class AllocationActivity implements Runnable {
		@Override
		public void run() {
			while (!stopped.get()) {
				byte[] block = new byte[blockSize];
				byteBlockAllocated(block);
			}
		}
	}
	
	public void start() {
		Thread allocThrd = new Thread(new AllocationActivity(), "byte-block-allocator");
		allocThrd.start();
	}
	
	public void stop() {
		stopped.set(true);
	}
	
	public void byteBlockAllocated(byte[] block) {
		hash.addAndGet(Arrays.hashCode(block));
	}
}
