package com.imc.rnd.lang.gowasm.ir.val;

/*
 * in wasm model, stored on operand stack, id means stack position, 0 - at the top
 */
public class Temp implements Var {
    private final int id;

    public Temp(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return "t:" + id;
    }

    @Override
    public ValKind getKind() {
        return ValKind.TEMP;
    }

    public String toString() {
        return "temp (" + id + ")";
    }
}
