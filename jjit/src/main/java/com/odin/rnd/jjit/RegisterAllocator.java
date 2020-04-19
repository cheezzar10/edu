package com.odin.rnd.jjit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.odin.rnd.jjit.dflow.ControlFlowGraph;
import com.odin.rnd.jjit.dflow.RegisterLivenessIntervalStartPosComparator;
import com.odin.rnd.jjit.dflow.RegisterLivenessIntervals;
import com.odin.rnd.jjit.ir.AddConstant;
import com.odin.rnd.jjit.ir.AddRegister;
import com.odin.rnd.jjit.ir.BranchIfGreaterOrEqual;
import com.odin.rnd.jjit.ir.Call;
import com.odin.rnd.jjit.ir.Instruction;
import com.odin.rnd.jjit.ir.MoveConstant;
import com.odin.rnd.jjit.ir.MoveRegister;
import com.odin.rnd.jjit.ir.PhysicalRegister;
import com.odin.rnd.jjit.ir.Return;
import com.odin.rnd.jjit.ir.VirtualRegister;

public class RegisterAllocator {
	public static void main(String[] args) {
		// this low-level IR is highly RISC-V specific
		List<Instruction> instructions = new ArrayList<>();
		
		// saving function argument
		instructions.add(new MoveRegister(new VirtualRegister(0), PhysicalRegister.X10));
		
		// if (%a0 < 2) return %a0
		instructions.add(new MoveConstant(new VirtualRegister(1), 2));
		instructions.add(new BranchIfGreaterOrEqual(new VirtualRegister(0), new VirtualRegister(1), 6));
		
		// return n
		instructions.add(new MoveRegister(PhysicalRegister.X10, new VirtualRegister(0)));
		instructions.add(new Return());
		
		// f1 = fib(n - 1)
		// n - 1
		instructions.add(new AddConstant(new VirtualRegister(2), new VirtualRegister(0), -1));
		// setting function argument
		instructions.add(new MoveRegister(PhysicalRegister.X10, new VirtualRegister(2)));
		// performing call
		instructions.add(new Call("fib", PhysicalRegister.X10));
		// saving function return value
		instructions.add(new MoveRegister(new VirtualRegister(3), PhysicalRegister.X10));
		
		
		// f2 = fib(n - 2)
		// n - 2
		instructions.add(new AddConstant(new VirtualRegister(4), new VirtualRegister(0), -2));
		// setting function argument
		instructions.add(new MoveRegister(PhysicalRegister.X10, new VirtualRegister(4)));
		// call
		instructions.add(new Call("fib", PhysicalRegister.X10));
		// saving return value
		instructions.add(new MoveRegister(new VirtualRegister(5), PhysicalRegister.X10));
		
		// calculating f = f1 + f2
		instructions.add(new AddRegister(new VirtualRegister(6), new VirtualRegister(3), new VirtualRegister(5)));
		
		// returning f
		instructions.add(new MoveRegister(PhysicalRegister.X10, new VirtualRegister(6)));
		instructions.add(new Return());
		
		RegisterAllocator allocator = new RegisterAllocator();
		allocator.allocateRegisters(instructions);
	}
	
	public void allocateRegisters(List<Instruction> instructions) {
		ControlFlowGraph controlFlowGraph = buildControlFlowGraph(instructions);
		
		LinkedList<RegisterLivenessIntervals> unhandledIntervals = new LinkedList<>(
				controlFlowGraph.getLivenessIntervals().stream()
				.filter(rli -> rli.getRegister() instanceof VirtualRegister)
				.collect(Collectors.toSet())
		);
		Collections.sort(unhandledIntervals, new RegisterLivenessIntervalStartPosComparator());
		
		Set<RegisterLivenessIntervals> activeIntervals = new HashSet<>();
		Set<RegisterLivenessIntervals> handledIntervals = new HashSet<>();
		
		while (!unhandledIntervals.isEmpty()) {
			RegisterLivenessIntervals currInterval = unhandledIntervals.removeFirst();
			int currIntervalStart = currInterval.getFirstIntervalStart();
			
			for (Iterator<RegisterLivenessIntervals> activeIntervalsIter = activeIntervals.iterator();activeIntervalsIter.hasNext();) {
				RegisterLivenessIntervals activeInterval = activeIntervalsIter.next();
				if (activeInterval.getLastIntervalEnd() < currIntervalStart) {
					activeIntervalsIter.remove();

					handledIntervals.add(activeInterval);
				}
			}
			
			boolean allocated = allocateRegister(currInterval, activeIntervals);
			if (allocated) {
				activeIntervals.add(currInterval);
			} else {
				throw new IllegalStateException("failed to allocate register for: " + currInterval);
			}
		}

		dump(instructions, controlFlowGraph);
	}

	private boolean allocateRegister(RegisterLivenessIntervals interval, Set<RegisterLivenessIntervals> activeIntervals) {
		int[] freeRegPos = new int[4];
		Arrays.fill(freeRegPos, Integer.MAX_VALUE);
		
		for (RegisterLivenessIntervals activeInterval : activeIntervals) {
			freeRegPos[activeInterval.getAllocatedRegisterId()] = 0;
		}
		
		int freeRegId = findFreeRegister(freeRegPos);
		if (freeRegPos[freeRegId] == 0) {
			return false;
		} else if (freeRegPos[freeRegId] > interval.getLastIntervalEnd()) {
			interval.setAllocatedRegisterId(freeRegId);
		}
		
		return true;
	}

	private int findFreeRegister(int[] freeRegPos) {
		int maxIdx = 0;
		for (int i = 0; i < freeRegPos.length;i++) {
			if (freeRegPos[i] > freeRegPos[maxIdx]) {
				maxIdx = i;
			}
		}
		
		return maxIdx;
	}

	private ControlFlowGraph buildControlFlowGraph(List<Instruction> instructions) {
		ControlFlowGraph controlFlowGraph = ControlFlowGraph.build(instructions);
		controlFlowGraph.computeLiveness(instructions);
		
		return controlFlowGraph;
	}

	private void dump(List<Instruction> instructions, ControlFlowGraph controlFlowGraph) {
		System.out.printf("control flow graph:%n%n");
		for (int instrId = 1; instrId <= instructions.size(); instrId++) {
			Instruction instr = instructions.get(instrId - 1);
			System.out.printf("%d: %s%n", instrId, instr);

			System.out.printf("%d: pred: %s%n", instrId, controlFlowGraph.getPred(instrId));
			System.out.printf("%d: succ: %s%n", instrId, controlFlowGraph.getSucc(instrId));
			
			System.out.printf("%d: live in: %s%n", instrId, controlFlowGraph.getLiveIn(instrId));
			System.out.printf("%d: live out: %s%n", instrId, controlFlowGraph.getLiveOut(instrId));
			
			System.out.println();
		}
		
		System.out.println("\nregister liveness intervals");
		for (RegisterLivenessIntervals rli : controlFlowGraph.getLivenessIntervals()) {
			System.out.printf("%s%n", rli);
		}
	}
}
