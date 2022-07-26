package com.imc.rnd.lang.gowasm.ir.op;

import com.imc.rnd.lang.gowasm.ir.val.Val;

// var a int = 10
public class LocalDef extends BaseOp {
    private final String name;
    private final Val initialValue;

    public LocalDef(String name, Val initialValue) {
        this.name = name;
        this.initialValue = initialValue;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return super.toString() + ": local def (`" + name + "` <- (" + initialValue + ")";
    }

    @Override
    public OpCode getOpCode() {
        return OpCode.LOCAL_DEF;
    }
}
