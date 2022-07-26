package com.imc.rnd.lang.gowasm.ir.val;

import java.util.Objects;
import java.util.Optional;

public class Local implements Var {
    // type etc can be retrieved from var scope symbol table
    private final String name;

    public Local(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public Optional<String> getName() {
        return Optional.of(name);
    }

    @Override
    public ValKind getKind() {
        return ValKind.LOCAL;
    }

    public String toString() {
        return "local var (`" + name + "`)";
    }
}
