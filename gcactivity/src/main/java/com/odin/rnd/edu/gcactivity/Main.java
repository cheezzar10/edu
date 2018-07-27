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
	private static final ByteBlockAllocator byteBlockAllocator = new ByteBlockAllocator(16 * 1024);
	
	private static final Deque<byte[]> memBlocksStack = new ArrayDeque<>(16);
	
	// TODO to enum
	private static final int KB = 1024;
	private static final int MB = 1024 * KB;
	
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
			case "memstat":
				printMemStat();
				break;
			case "allocate":
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
		
		out.printf("allocating memory block of size: %d bytes%n", blockSize);
		
		byte[] byteBlk = new byte[blockSize];
		memBlocksStack.addFirst(byteBlk);
	}

	private static int parseBlockSize(String blkSz) {
		out.printf("parsing memory block size argument: %s%n", blkSz);
		
		Matcher matcher = BLOCK_SIZE_PTRN.matcher(blkSz);
		if (matcher.matches()) {
			if (matcher.groupCount() > 2) {
				String unit = matcher.group(2);
				out.printf("unit: %s%n", unit);
			}
		}
		
		return MB;
	}

	private static CommandWithArgs parseCommand(String cmd) {
		String[] cmdArgs = cmd.split("\\s+", 2);
		return new CommandWithArgs(cmdArgs[0], cmdArgs.length > 1 ? cmdArgs[1].split("\\s+") : new String[] {});
	}

	private static void printMemStat() {
		Runtime rt = Runtime.getRuntime();
		out.printf("max mem: %d, total mem: %d, free mem: %d%n", rt.maxMemory(), rt.totalMemory(), rt.freeMemory());
	}
}