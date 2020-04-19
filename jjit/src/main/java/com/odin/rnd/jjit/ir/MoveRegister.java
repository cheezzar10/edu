package com.odin.rnd.jjit.ir;

import java.util.Collections;
import java.util.Set;

public class MoveRegister implements Instruction {
	private final Register dstReg;
	private final Register srcReg;

	public MoveRegister(Register dstReg, Register srcReg) {
		this.dstReg = dstReg;
		this.srcReg = srcReg;
	}
	
	public Register getDestinationRegister() {
		return dstReg;
	}
	
	public Register getSourceRegister() {
		return srcReg;
	}

	@Override
	public Set<Register> getDefinedRegisters() {
		return Collections.singleton(dstReg);
	}

	@Override
	public Set<Register> getUsedRegisters() {
		return Collections.singleton(srcReg);
	}
	
	public String toString() {
		return "mov " + dstReg + ", " + srcReg;
	}
}
