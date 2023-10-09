package com.imc.rnd.lang.gowasm.ir.op;

import java.util.Objects;

public class Label extends BaseOp {
    private final String name;

    @Override
    public OpCode getOpCode() {
        return OpCode.LABEL;
    }

    public Label(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isTargetFor(Op op) {
        var result = switch (op.getOpCode()) {
            case JUMP -> ((Jump)op).getTargetLabel().equals(this);
            case COND_JUMP -> ((CondJump)op).getTargetLabel().equals(this);
            default -> false;
        };

        return result;
    }

    public String toString() {
        return super.toString() + ": label `" + name + "`";
    }

    public boolean equals(Object thatObj) {
        if (this == thatObj) {
            return true;
        }

        if (!(thatObj instanceof Label)) {
            return false;
        }

        var that = (Label) thatObj;

        return Objects.equals(that.name, name);
    }

    public int hashCode() {
        return Objects.hashCode(name);
    }
}
