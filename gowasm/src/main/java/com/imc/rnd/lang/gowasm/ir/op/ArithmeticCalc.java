package com.imc.rnd.lang.gowasm.ir.op;

import com.imc.rnd.lang.gowasm.ir.val.Val;
import com.imc.rnd.lang.gowasm.ir.val.Var;

// TODO rename to ArithmeticExpr
public class ArithmeticCalc extends BaseOp {
    private final ArithmeticOp operator;
    private final Var target;
    private final Val leftOperand;
    private final Val rightOperand;

    public ArithmeticCalc(ArithmeticOp operator, Var target, Val leftOperand, Val rightOperand) {
        this.operator = operator;
        this.target = target;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
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
