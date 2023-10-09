package com.imc.rnd.lang.gowasm.ir.op;

import com.imc.rnd.lang.gowasm.ir.val.Val;
import com.imc.rnd.lang.gowasm.ir.val.Var;

public class Move extends BaseOp {
    private final Var target;
    private final Val source;

    public Move(Var target, Val source) {
        this.target = target;
        this.source = source;
    }

    public Var getTarget() {
        return target;
    }

    public Val getSource() {
        return source;
    }

    public String toString() {
        return super.toString() + ": move " + target + " = " + source;
    }

    @Override
    public OpCode getOpCode() {
        return OpCode.MOVE;
    }
}
