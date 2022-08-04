package com.imc.rnd.lang.gowasm.ir.op;

import com.imc.rnd.lang.gowasm.ir.val.Val;
import com.imc.rnd.lang.gowasm.ir.val.Var;

public class Comparison extends BaseOp {
    private final RelOp operator;
    private final Var target;
    private final Val leftOperand;
    private final Val rightOperand;

    public Comparison(RelOp operator, Var target, Val leftOperand, Val rightOperand) {
        this.operator = operator;
        this.target = target;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public RelOp getOperator() {
        return operator;
    }

    public Var getTarget() {
        return target;
    }

    public Val getLeftOperand() {
        return leftOperand;
    }

    public Val getRightOperand() {
        return rightOperand;
    }

    public String toString() {
        return super.toString() +
                ": cmp op (" + target + " <- " + leftOperand + " " + operator + " " + rightOperand + ")";
    }

    @Override
    public OpCode getOpCode() {
        return OpCode.COMPARISON;
    }
}
