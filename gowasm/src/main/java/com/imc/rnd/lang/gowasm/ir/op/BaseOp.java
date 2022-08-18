package com.imc.rnd.lang.gowasm.ir.op;

import java.util.Optional;

public abstract class BaseOp implements Op {
    private int id;

    public int getId() {
        return id;
    }

    public Op withId(int id) {
        this.id = id;
        return this;
    }

    public String toString() {
        return "" + id;
    }
}
