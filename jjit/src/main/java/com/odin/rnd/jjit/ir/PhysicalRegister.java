package com.odin.rnd.jjit.ir;

import java.util.Set;

// TODO convert to enumeration containing all physical registers
public class PhysicalRegister implements Register {
	public static final PhysicalRegister X10 = new PhysicalRegister(10);
	
	public static final PhysicalRegister T0 = new PhysicalRegister(5);
	public static final PhysicalRegister T1 = new PhysicalRegister(6);
	public static final PhysicalRegister T2 = new PhysicalRegister(7);
	public static final PhysicalRegister T3 = new PhysicalRegister(28);
	
	public static final Set<PhysicalRegister> temp_regs = Set.of(T0, T1, T2, T3);
	
	private final int id;

	public PhysicalRegister(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		
		if (!(other instanceof PhysicalRegister)) {
			return false;
		}
		
		PhysicalRegister that = (PhysicalRegister) other;
		return id == that.id;
	}
	
	public int hashCode() {
		return id;
	}
	
	public String toString() {
		return "%x" + id;
	}
}