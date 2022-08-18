package com.imc.rnd.lang.gowasm.ir.op;

import com.imc.rnd.lang.gowasm.ir.val.Val;

public class ReturnVal extends BaseOp {
    private Val value;

    public ReturnVal(Val value) {
        this.value = value;
    }

    public Val getValue() {
        return value;
    }

    @Override
    public OpCode getOpCode() {
        return OpCode.RETURN_VAL;
    }
}
