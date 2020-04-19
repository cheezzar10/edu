package com.odin.rnd.edu.gcactivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

class Main {
	// TODO to enum
	private static final int KB = 1024;
	private static final int MB = 1024 * KB;
	private static final int GB = 1024 * MB;
	
	// to AllocatorCli instance
	private static final ByteBlockAllocator byteBlockAllocator = new ByteBlockAllocator("byte-block-allocator", 128 * KB);
	
	private static final Deque<byte[]> memBlocksStack = new ArrayDeque<>(16);
	
	private static final int INVALID_BLOCK_SIZE = -1;
	
	private static final Pattern BLOCK_SIZE_PTRN = Pattern.compile("(\\d+)([kmg]?)");
	
	private static class CommandWithArgs {
		private final String command;
		private final List<String> arguments;
		
		public CommandWithArgs(String cmd, String[] args) {
			command = cmd;
			arguments = new ArrayList<>(Arrays.asList(args));
		}

		public String getCommand() {
			return command;
		}
		
		public List<String> getArguments() {
			return Collections.unmodifiableList(arguments);
		}
	}
	
	public static void main(String[] args) throws Exception {
		out.println("started");
		
		printMemStat();
		byteBlockAllocator.start();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		// command handling loop
		CMDLOOP: for (;;) {
			out.print("> ");
			
			CommandWithArgs cmdArgs = parseCommand(in.readLine());
			switch (cmdArgs.getCommand()) {
			case "stop":
				byteBlockAllocator.stop();
				break CMDLOOP;
			case "mem":
				printMemStat();
				break;
			case "alloc":
				allocate(cmdArgs.getArguments());
				break;
			default:
				out.printf("unknown command: %s%n", cmdArgs.getCommand());
				break;
			}
		}
		
		out.println("stopped");
	}

	private static void allocate(List<String> args) {
		int blockSize = MB;
		if (!args.isEmpty()) {
			blockSize = parseBlockSize(args.get(0));
		}
		
		if (blockSize <= 0) {
			return;
		}
		
		int blocksCount = 1;
		if (args.size() > 1) {
			blocksCount = Integer.parseInt(args.get(1));
		}
		
		out.printf("allocating %d memory block(s) of size: %,d bytes%n", blocksCount, blockSize);
		
		for (int i = 0;i < blocksCount;i++) {
			byte[] byteBlk = new byte[blockSize];
			memBlocksStack.addFirst(byteBlk);
		}
	}

	private static int parseBlockSize(String blkSz) {
		Matcher matcher = BLOCK_SIZE_PTRN.matcher(blkSz);
		if (matcher.matches()) {
			int size = Integer.parseInt(matcher.group(1));
			
			String unit = matcher.group(2);
			if (!unit.isEmpty()) {
				switch (unit) {
				case "k":
					return size * KB;
				case "m":
					return size * MB;
				case "g":
					return size * GB;
				default:
					out.printf("unknown unit type: %s%n", unit);
					return INVALID_BLOCK_SIZE;
				}
			} 
			
			return size;
		}
		
		out.printf("invalid block size format: %s%n", blkSz);
		return INVALID_BLOCK_SIZE;
	}

	private static CommandWithArgs parseCommand(String cmd) {
		String[] cmdArgs = cmd.split("\\s+", 2);
		return new CommandWithArgs(cmdArgs[0], cmdArgs.length > 1 ? cmdArgs[1].split("\\s+") : new String[] {});
	}

	private static void printMemStat() {
		Runtime rt = Runtime.getRuntime();
		out.printf("max mem: %,d, total mem: %,d, free mem: %,d%n", rt.maxMemory(), rt.totalMemory(), rt.freeMemory());
	}
}