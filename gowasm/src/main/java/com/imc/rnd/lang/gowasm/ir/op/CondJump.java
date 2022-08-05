package com.imc.rnd.lang.gowasm.ir.op;

import com.imc.rnd.lang.gowasm.ir.val.Val;

// TODO do not store operands, should reference boolean val
// TODO Split conditional branch into Branch ( forward jump ) and Loop ( backward jump )
public class CondJump extends BaseOp {
    private final Val jumpCondition;
    private final Label falseBranchLabel;

    public CondJump(Val jumpCondition, Label falseBranchLabel) {
        this.jumpCondition = jumpCondition;
        this.falseBranchLabel = falseBranchLabel;
    }

    public Val getJumpCondition() {
        return jumpCondition;
    }

    public Label getFalseBranchLabel() {
        return falseBranchLabel;
    }

    public String toString() {
        return super.toString() +
                ": cond jump ((" + jumpCondition + ") |-> " + falseBranchLabel + ")";
    }

    @Override
    public OpCode getOpCode() {
        return OpCode.COND_JUMP;
    }
}
