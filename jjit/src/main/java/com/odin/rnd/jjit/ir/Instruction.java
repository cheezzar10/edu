package com.odin.rnd.jjit.ir;

import java.util.Set;

public interface Instruction {
	Set<Register> getDefinedRegisters();

	Set<Register> getUsedRegisters();
}
