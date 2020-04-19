package com.odin.rnd.jjit.ir;

import java.util.Collections;
import java.util.Set;

public class Def implements Instruction {
	private final Register reg;

	public Def(Register reg) {
		this.reg = reg;
	}

	@Override
	public Set<Register> getDefinedRegisters() {
		return Collections.singleton(reg);
	}

	@Override
	public Set<Register> getUsedRegisters() {
		return Collections.emptySet();
	}

	public String toString() {
		return "def: " + reg;
	}
}
