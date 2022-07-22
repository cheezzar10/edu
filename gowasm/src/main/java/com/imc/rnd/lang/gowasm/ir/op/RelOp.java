package com.imc.rnd.lang.gowasm.ir.op;

public enum RelOp {
    LT("<");

    private final String symbol;

    RelOp(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return "`" + symbol + "`";
    }
}
