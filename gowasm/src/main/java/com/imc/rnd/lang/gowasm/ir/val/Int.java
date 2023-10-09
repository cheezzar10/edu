package com.imc.rnd.lang.gowasm.ir.val;

public class Int implements Val {
    private final int value;

    public Int(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public ValKind getKind() {
        return ValKind.INT;
    }

    public String toString() {
        return value + " : int";
    }
}
