package com.odin.rnd.jjit.ir;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Call implements Instruction {
	private final String label;
	private Set<Register> argRegs;

	public Call(String label, PhysicalRegister... argRegs) {
		this.label = label;
		this.argRegs = new LinkedHashSet<>(Arrays.asList(argRegs));
	}
	
	public String getLabel() {
		return label;
	}

	@Override
	public Set<Register> getDefinedRegisters() {
		return Collections.singleton(PhysicalRegister.X10);
	}

	@Override
	public Set<Register> getUsedRegisters() {
		return argRegs;
	}
	
	public String toString() {
		String argRegsStr = argRegs.stream()
				.map(Register::toString)
				.collect(Collectors.joining(", ", "(", ")"));

		return "call " + label + argRegsStr;
	}
}
