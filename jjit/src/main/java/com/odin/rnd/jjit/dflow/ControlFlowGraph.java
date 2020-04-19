package com.odin.rnd.jjit.dflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.odin.rnd.jjit.ir.BranchIfGreaterOrEqual;
import com.odin.rnd.jjit.ir.Instruction;
import com.odin.rnd.jjit.ir.Register;
import com.odin.rnd.jjit.ir.Return;

public class ControlFlowGraph {
	private final List<Set<Integer>> predecessors = new ArrayList<>();
	private final List<Set<Integer>> successors = new ArrayList<>();
	
	private final List<Set<Register>> liveIn = new ArrayList<>();
	private final List<Set<Register>> liveOut = new ArrayList<>();
	
	private final Map<Register, RegisterLivenessIntervals> liveIntervals = new LinkedHashMap<>();

	public ControlFlowGraph(int instrListSize) {
		for (int instrIdx = 0; instrIdx < instrListSize; instrIdx++) {
			predecessors.add(new LinkedHashSet<>());
			successors.add(new LinkedHashSet<>());
			
			liveIn.add(new HashSet<>());
			liveOut.add(new HashSet<>());
		}
	}

	public Set<Integer> getPred(int instrId) {
		return predecessors.get(instrId - 1);
	}

	public Set<Integer> getSucc(int instrId) {
		return successors.get(instrId - 1);
	}

	public Set<Register> getLiveIn(int instrId) {
		return liveIn.get(instrId - 1);
	}

	public Set<Register> getLiveOut(int instrId) {
		return liveOut.get(instrId - 1);
	}

	public void addSucc(int instrId, Set<Integer> succInstrs) {
		successors.get(instrId - 1).addAll(succInstrs);
	}

	public void addPred(int instrId, Set<Integer> predInstrIds) {
		predecessors.get(instrId - 1).addAll(predInstrIds);
	}

	public boolean addLiveOut(int instrId, Set<Register> regs) {
		Set<Register> instrLiveOut = liveOut.get(instrId - 1);
		
		return instrLiveOut.addAll(regs);
	}

	public void setLiveIn(int instrId, Set<Register> liveIn) {
		this.liveIn.set(instrId - 1, liveIn);
	}

	public static ControlFlowGraph build(List<Instruction> instructions) {
		ControlFlowGraph controlFlowGraph = new ControlFlowGraph(instructions.size());
		
		for (int instrId = 1; instrId <= instructions.size(); instrId++) {
			Set<Integer> succInstrs = getSuccessors(instructions, instrId);
			
			controlFlowGraph.addSucc(instrId, succInstrs);
			
			for (Integer succInstrId : succInstrs) {
				controlFlowGraph.addPred(succInstrId, Collections.singleton(instrId));
			}
		}
		
		return controlFlowGraph;
	}

	private static Set<Integer> getSuccessors(List<Instruction> instructions, int instrId) {
		if (instrId == instructions.size()) {
			return Collections.emptySet();
		}
		
		Set<Integer> succs = new LinkedHashSet<>();
		Instruction instr = instructions.get(instrId - 1);
		if (instr instanceof Return) {
			return Collections.emptySet();
		} else if (instr instanceof BranchIfGreaterOrEqual) {
			BranchIfGreaterOrEqual beq = (BranchIfGreaterOrEqual) instr;
			
			// adding branch target instruction to successors list
			succs.add(beq.getTargetInstructionId());
		}
		
		// sequential execution successor
		succs.add(instrId + 1);
		
		return succs;
	}

	private void computeLivenessSets(List<Instruction> instructions) {
		while (true) {
			boolean liveOutChanged = false;

			for (int currInstrId = instructions.size(); currInstrId > 0; currInstrId--) {
				Instruction currInstr = instructions.get(currInstrId - 1);

				Set<Integer> instrSuccs = getSucc(currInstrId);
				for (Integer succInstrId : instrSuccs) {
					Set<Register>  succLiveIn = getLiveIn(succInstrId);
					boolean changed = addLiveOut(currInstrId, succLiveIn);
					
					liveOutChanged |= changed;
				}
				
				Set<Register> newLiveIn = new HashSet<>(getLiveOut(currInstrId));

				newLiveIn.removeAll(currInstr.getDefinedRegisters());
				newLiveIn.addAll(currInstr.getUsedRegisters());
				
				setLiveIn(currInstrId, newLiveIn);
			}
			
			if (!liveOutChanged) {
				break;
			}
		}
	}

	private void computeLivenessIntervals(List<Instruction> instructions) {
		for (int currInstrId = instructions.size(); currInstrId > 0; currInstrId--) {
			Set<Register> currLiveOut = getLiveOut(currInstrId);
			
			for (Register liveReg : currLiveOut) {
				addOrExtendLivenessInterval(liveReg, currInstrId, currInstrId);
			}
			
			Instruction currInstr = instructions.get(currInstrId - 1);
			for (Register writeReg : currInstr.getDefinedRegisters()) {
				RegisterLivenessIntervals regLivenessIntervals = liveIntervals.get(writeReg);
				if (regLivenessIntervals != null) {
					regLivenessIntervals.setFirstIntervalStart(currInstrId);
					regLivenessIntervals.addUsePosition(currInstrId);
				}
			}

			for (Register readReg : currInstr.getUsedRegisters()) {
				addOrExtendLivenessInterval(readReg, currInstrId, currInstrId);
				
				RegisterLivenessIntervals regLivenessIntervals = liveIntervals.get(readReg);
				if (regLivenessIntervals != null) {
					regLivenessIntervals.addUsePosition(currInstrId);
				}
			}
		}
	}
	
	private RegisterLivenessInterval addOrExtendLivenessInterval(Register reg, int startInstrId, int endInstrId) {
		RegisterLivenessIntervals regLivenessIntervals = liveIntervals.get(reg);
		if (regLivenessIntervals == null) {
			return liveIntervals.computeIfAbsent(reg, RegisterLivenessIntervals::new)
					.addLivenessInterval(startInstrId, endInstrId);
		}

		RegisterLivenessInterval extendedInterval = extendInterval(regLivenessIntervals, startInstrId, endInstrId);
		if (extendedInterval != null) {
			return extendedInterval;
		}
		
		return regLivenessIntervals.addLivenessInterval(startInstrId, endInstrId);
	}
	
	private RegisterLivenessInterval extendInterval(RegisterLivenessIntervals regLivenessIntervals, int startInstrId, int endInstrId) {
		for (RegisterLivenessInterval livenessInterval : regLivenessIntervals.getLivenessIntervals()) {
			if (getSucc(livenessInterval.getEndInstrId()).contains(startInstrId)) {
				livenessInterval.setEndInstrId(endInstrId);
				return livenessInterval;
			} else if (getPred(livenessInterval.getStartInstrId()).contains(endInstrId)) {
				livenessInterval.setStartInstrId(startInstrId);
				return livenessInterval;
			}
		}
		
		return null;
	}

	public void computeLiveness(List<Instruction> instructions) {
		computeLivenessSets(instructions);
		
		computeLivenessIntervals(instructions);
		
		for (RegisterLivenessIntervals regLivenessIntervals : liveIntervals.values()) {
			regLivenessIntervals.mergeOverlapping();
		}
	}

	public Collection<RegisterLivenessIntervals> getLivenessIntervals() {
		return liveIntervals.values();
	}
}
