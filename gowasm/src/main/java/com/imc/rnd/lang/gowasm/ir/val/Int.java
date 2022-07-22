package com.imc.rnd.lang.gowasm.ir.val;

import java.util.Optional;

public class Int implements Val {
    private final int value;

    public Int(int value) {
        this.value = value;
    }

    @Override
    public Optional<String> getName() {
        return Optional.empty();
    }

    public String toString() {
        return "int (" + value + ")";
    }
}
