package com.odin.rnd.jjit.ir;

import java.util.Collections;
import java.util.Set;

public class Return implements Instruction {
	public static final Object INSTANCE = new Return();

	private final Set<Register> usedReg = Set.of(PhysicalRegister.X10);

	@Override
	public Set<Register> getDefinedRegisters() {
		return Collections.emptySet();
	}

	@Override
	public Set<Register> getUsedRegisters() {
		return usedReg;
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		
		if (!(other instanceof Return)) {
			return false;
		}
		
		return true;
	}
	
	public int hashCode() {
		return 17;
	}

	public String toString() {
		return "ret";
	}
}
