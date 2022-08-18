package com.imc.rnd.lang.gowasm.ir.op;

import com.imc.rnd.lang.gowasm.ir.val.Val;
import com.imc.rnd.lang.gowasm.ir.val.Var;

// TODO rename to ArithmeticExpr
public class ArithmeticCalc extends BaseOp {
    private final Var target;
    private final Val leftOperand;
    private final ArithmeticOp operator;
    private final Val rightOperand;

    public ArithmeticCalc(Var target, Val leftOperand, ArithmeticOp operator, Val rightOperand) {
        this.target = target;
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public Var getTarget() {
        return target;
    }

    public Val getLeftOperand() {
        return leftOperand;
    }

    public ArithmeticOp getOperator() {
        return operator;
    }

    public Val getRightOperand() {
        return rightOperand;
    }

    public String toString() {
        return super.toString() +
                ": math op (" + target + " <- " + leftOperand + " " + operator + " " + rightOperand + ")";
    }

    @Override
    public OpCode getOpCode() {
        return OpCode.ARITHMETIC_CALC;
    }
}
