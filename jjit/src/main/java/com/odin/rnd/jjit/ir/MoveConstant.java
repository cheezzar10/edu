package com.odin.rnd.jjit.ir;

import java.util.Collections;
import java.util.Set;

public class MoveConstant implements Instruction {
	private final Register reg;
	private final int val;

	private final Set<Register> defRegs;

	public MoveConstant(Register reg, int val) {
		this.reg = reg;
		this.val = val;
		
		this.defRegs = Collections.singleton(reg);
	}
	
	public Register getRegister() {
		return reg;
	}
	
	public int getValue() {
		return val;
	}

	@Override
	public Set<Register> getDefinedRegisters() {
		return defRegs;
	}
	
	@Override
	public Set<Register> getUsedRegisters() {
		return Collections.emptySet();
	}

	public String toString() {
		return "mov " + reg + ", $" + val;
	}
}
