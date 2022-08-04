package com.imc.rnd.lang.gowasm.ir.val;

import java.util.Objects;

public class Local implements Var {
    // type etc can be retrieved from var scope symbol table
    private final String name;

    public Local(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ValKind getKind() {
        return ValKind.LOCAL;
    }

    public String toString() {
        return "local var (`" + name + "`)";
    }
}
