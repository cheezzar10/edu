package com.odin.rnd.jjit.ir;

public class VirtualRegister implements Register {
	private final int id;

	public VirtualRegister(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		
		if (!(other instanceof VirtualRegister)) {
			return false;
		}
		
		VirtualRegister that = (VirtualRegister) other;
		return id == that.id;
	}
	
	public int hashCode() {
		return id;
	}
	
	public String toString() {
		return "%v" + id;
	}
}
