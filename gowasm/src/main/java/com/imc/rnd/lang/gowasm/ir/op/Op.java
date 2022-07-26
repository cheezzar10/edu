package com.imc.rnd.lang.gowasm.ir.op;

public interface Op {
    int getId();

    Op withId(int id);

    OpCode getOpCode();
}