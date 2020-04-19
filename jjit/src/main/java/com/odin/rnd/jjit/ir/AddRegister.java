package com.odin.rnd.jjit.ir;

import java.util.Collections;
import java.util.Set;

public class AddRegister implements Instruction {
	private final Register resReg;
	private final Register opReg1;
	private final Register opReg2;

	private final Set<Register> defRegs;
	private final Set<Register> usedReg;

	public AddRegister(Register resReg, Register opReg1, Register opReg2) {
		this.resReg = resReg;
		this.opReg1 = opReg1;
		this.opReg2 = opReg2;
		
		this.defRegs = Collections.singleton(resReg);
		this.usedReg = Set.of(opReg1, opReg2);
	}
	
	public Register getResultRegister() {
		return resReg;
	}
	
	public Register getOperandRegister1() {
		return opReg1;
	}
	
	public Register getOperandRegister2() {
		return opReg2;
	}

	@Override
	public Set<Register> getDefinedRegisters() {
		return defRegs;
	}

	@Override
	public Set<Register> getUsedRegisters() {
		return usedReg;
	}

	public String toString() {
		return "add " + resReg + ", " + opReg1 + ", " + opReg2;
	}
}
