package com.odin.rnd.edu.gcactivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ByteBlockAllocator {
	private final String name;
	private final int blockSize;
	
	private final AtomicInteger hash = new AtomicInteger();
	
	private final AtomicBoolean stopped = new AtomicBoolean();
	private final Random rnd = new Random();
	
	private int allocCount =  0;
	private long start = System.currentTimeMillis();
	
	private final PrintStream out;
	
	public ByteBlockAllocator(String nm, int blkSz) {
		blockSize = blkSz;
		name = nm;
		
		try {
			out = new PrintStream(new FileOutputStream(name + ".log"));
		} catch (FileNotFoundException fnfe) {
			throw new IllegalStateException("failed to open allocator log file: ", fnfe);
		}
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
		Thread allocThrd = new Thread(new AllocationActivity(), name);
		allocThrd.start();
	}
	
	public void stop() {
		stopped.set(true);
		out.close();
	}
	
	public void byteBlockAllocated(byte[] block) {
		// consuming more compute power to slow allocations down
		rnd.nextBytes(block);
		Arrays.sort(block);
		
		hash.addAndGet(Arrays.hashCode(block));
		
		// print allocation speed stat every second
		long time = System.currentTimeMillis();
		if (time - start > 1000) {
			out.printf("allocation speed: %d/s%n", allocCount);
			
			start = time;
			allocCount = 0;
		} else {
			allocCount++;
		}
	}
}
