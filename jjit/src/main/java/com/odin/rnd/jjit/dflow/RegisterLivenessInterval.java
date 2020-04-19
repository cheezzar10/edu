package com.odin.rnd.jjit.dflow;

public class RegisterLivenessInterval {
	private int startInstrId;
	private int endInstrId;

	public RegisterLivenessInterval(int startInstrId, int endInstrId) {
		this.startInstrId = startInstrId;
		this.endInstrId = endInstrId;
	}
	
	public int getStartInstrId() {
		return startInstrId;
	}

	public void setStartInstrId(int startInstrId) {
		this.startInstrId = startInstrId;
	}

	public int getEndInstrId() {
		return endInstrId;
	}
	
	public void setEndInstrId(int endInstrId) {
		this.endInstrId = endInstrId;
	}
	
	public String toString() {
		return "[" + startInstrId + ", " + endInstrId + "]";
	}
}
