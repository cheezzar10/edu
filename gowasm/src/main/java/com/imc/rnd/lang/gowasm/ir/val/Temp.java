package com.imc.rnd.lang.gowasm.ir.val;

import java.util.Optional;

/*
 * in wasm model, stored on operand stack, id means stack position, 0 - at the top
 */
public class Temp implements Var {
    private final int id;

    public Temp(int id) {
        this.id = id;
    }

    @Override
    public Optional<String> getName() {
        return Optional.of("t:" + id);
    }

    public String toString() {
        return "temp (" + id + ")";
    }
}
