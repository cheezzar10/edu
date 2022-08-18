package com.imc.rnd.lang.gowasm.ir.op;

public class Label extends BaseOp {
    private final String name;

    @Override
    public OpCode getOpCode() {
        return OpCode.LABEL;
    }

    public enum JumpDir {
        FORWARD, BACKWARD
    }

    private final JumpDir jumpDir;

    public Label(String name) {
        this.name = name;
        this.jumpDir = JumpDir.FORWARD;
    }

    public Label(String name, JumpDir jumpDir) {
        this.name = name;
        this.jumpDir = jumpDir;
    }

    public String getName() {
        return name;
    }

    public JumpDir getJumpDir() {
        return jumpDir;
    }

    public String toString() {
        return super.toString() + ": label `" + name + "`";
    }
}
