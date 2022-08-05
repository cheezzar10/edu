package com.imc.rnd.lang.gowasm.ir.op;

import java.util.Optional;

public abstract class BaseOp implements Op {
    private int id;
    private Optional<Label> label = Optional.empty();

    public int getId() {
        return id;
    }

    public Op withId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public Optional<Label> getLabel() {
        return label;
    }

    @Override
    public Op withLabel(Label label) {
        this.label = Optional.of(label);
        return this;
    }

    public String toString() {
        return "" + id;
    }
}
