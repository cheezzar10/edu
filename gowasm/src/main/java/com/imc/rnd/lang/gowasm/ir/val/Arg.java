package com.imc.rnd.lang.gowasm.ir.val;

import java.util.Objects;

public class Arg implements Var {
    private final String name;

    public Arg(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ValKind getKind() {
        return ValKind.ARG;
    }

    public String toString() {
        return "arg (`" + name + "`)";
    }
}
