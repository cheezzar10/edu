package com.imc.rnd.lang.gowasm.ir;

import com.imc.rnd.lang.gowasm.ir.op.Op;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LocalIRBuffer {
    private final ArrayList<Op> operations = new ArrayList<>();

    public void addOperation(Op operation) {
        int currentOperationId = operations.size();

        operations.add(operation.withId(currentOperationId));
    }

    public ArrayList<Op> getOperations() {
        return operations;
    }

    public String dumpOperationsToString() {
        return operations.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }
}
