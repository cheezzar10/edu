package com.imc.rnd.lang.gowasm.ir.op;

public enum ArithmeticOp {
    PLUS("+"), MINUS("-");

    private final String symbol;

    ArithmeticOp(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return "`" + symbol + "`";
    }
}
