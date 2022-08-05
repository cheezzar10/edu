package com.imc.rnd.lang.gowasm.ir.op;

public class Label {
    private final String name;

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
