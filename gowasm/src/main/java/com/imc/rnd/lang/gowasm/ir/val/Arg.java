package com.imc.rnd.lang.gowasm.ir.val;

import java.util.Objects;
import java.util.Optional;

public class Arg implements Val {
    private final String name;

    public Arg(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public Optional<String> getName() {
        return Optional.of(name);
    }

    @Override
    public ValKind getKind() {
        return ValKind.ARG;
    }

    public String toString() {
        return "arg (`" + name + "`)";
    }
}
