package com.imc.rnd.lang.gowasm.ir.op;

public class Label {
    private final String label;

    public Label(String label) {
        this.label = label;
    }

    public String toString() {
        return "label `" + label + "`";
    }
}
