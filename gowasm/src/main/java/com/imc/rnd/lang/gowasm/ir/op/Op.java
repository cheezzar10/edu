package com.imc.rnd.lang.gowasm.ir.op;

import java.util.Optional;

public interface Op {
    int getId();

    Op withId(int id);

    OpCode getOpCode();
}