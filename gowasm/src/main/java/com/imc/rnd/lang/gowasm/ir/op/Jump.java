package com.imc.rnd.lang.gowasm.ir.op;

public class Jump extends BaseOp {
	private final Label targetLabel;

	public Jump(Label targetLabel) {
		this.targetLabel = targetLabel;
	}

	@Override
	public OpCode getOpCode() {
		return OpCode.JUMP;
	}

	public Label getTargetLabel() {
		return targetLabel;
	}

	public String toString() {
		return super.toString() + ": jump --> " + targetLabel;
	}
}
