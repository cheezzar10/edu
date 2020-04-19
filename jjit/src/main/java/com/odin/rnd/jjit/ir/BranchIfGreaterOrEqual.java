package com.odin.rnd.jjit.ir;

import java.util.Collections;
import java.util.Set;

public class BranchIfGreaterOrEqual implements Instruction {
	private final Register opReg1;
	private final Register opReg2;

	private final int targetInstrId;

	private final Set<Register> usedReg;

	public BranchIfGreaterOrEqual(Register opReg1, Register opReg2, int targetInstrId) {
		this.opReg1 = opReg1;
		this.opReg2 = opReg2;
		
		this.targetInstrId = targetInstrId;
		
		this.usedReg = Set.of(opReg1, opReg2);
	}
	
	public Register getOperandRegister1() {
		return opReg1;
	}
	
	public Register getOperandRegister2() {
		return opReg2;
	}
	
	public int getTargetInstructionId() {
		return targetInstrId;
	}

	public Set<Register> getDefinedRegisters() {
		return Collections.emptySet();
	}

	@Override
	public Set<Register> getUsedRegisters() {
		return usedReg;
	}
	
	public String toString() {
		return "bge " + opReg1 + ", " + opReg2 + ", " + targetInstrId;
	}
}
