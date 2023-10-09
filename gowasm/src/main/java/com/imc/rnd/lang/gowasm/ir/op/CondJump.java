package com.imc.rnd.lang.gowasm.ir.op;

import com.imc.rnd.lang.gowasm.ir.val.Val;

public class CondJump extends BaseOp {
    private final Val leftOperand;
    private final RelOp operator;
    private final Val rightOperand;
    private final Label targetLabel;

    public CondJump(Val leftOperand, RelOp operator, Val rightOperand, Label targetLabel) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.targetLabel = targetLabel;
    }

    public Val getLeftOperand() {
        return leftOperand;
    }

    public RelOp getOperator() {
        return operator;
    }

    public Val getRightOperand() {
        return rightOperand;
    }

    public Label getTargetLabel() {
        return targetLabel;
    }

    public String toString() {
        return super.toString() +
                ": cond jump (" + leftOperand + " " + operator + " " + rightOperand + ") --> " + targetLabel;
    }

    @Override
    public OpCode getOpCode() {
        return OpCode.COND_JUMP;
    }
}
