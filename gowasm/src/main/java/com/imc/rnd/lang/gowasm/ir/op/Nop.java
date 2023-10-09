package com.imc.rnd.lang.gowasm.ir.op;

public class Nop extends BaseOp {
	@Override
	public OpCode getOpCode() {
		return OpCode.NOP;
	}

	public String toString() {
		return super.toString() + ": nop";
	}
}
