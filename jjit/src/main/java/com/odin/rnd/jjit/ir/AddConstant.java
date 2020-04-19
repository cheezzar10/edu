package com.odin.rnd.jjit.ir;

import java.util.Collections;
import java.util.Set;

public class AddConstant implements Instruction {
	private final Register resReg;
	private final Register opReg;
	private final int opVal;

	public AddConstant(Register resReg, Register opReg, int opVal) {
		this.resReg = resReg;
		this.opReg = opReg;
		this.opVal = opVal;
	}
	
	public Register getResultRegister() {
		return resReg;
	}
	
	public Register getOperandRegister() {
		return opReg;
	}
	
	public int getOperandValue() {
		return opVal;
	}

	@Override
	public Set<Register> getDefinedRegisters() {
		return Collections.singleton(resReg);
	}

	@Override
	public Set<Register> getUsedRegisters() {
		return Collections.singleton(opReg);
	}
	
	public String toString() {
		return "addi " + resReg + ", " + opReg + ", " + opVal;
	}
}
