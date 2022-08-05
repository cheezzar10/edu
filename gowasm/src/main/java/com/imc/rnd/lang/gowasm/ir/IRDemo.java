package com.imc.rnd.lang.gowasm.ir;

import com.imc.rnd.lang.gowasm.ir.op.ArithmeticCalc;
import com.imc.rnd.lang.gowasm.ir.op.Comparison;
import com.imc.rnd.lang.gowasm.ir.op.CondJump;
import com.imc.rnd.lang.gowasm.ir.op.Label;
import com.imc.rnd.lang.gowasm.ir.op.LocalDef;
import com.imc.rnd.lang.gowasm.ir.op.ArithmeticOp;
import com.imc.rnd.lang.gowasm.ir.op.Move;
import com.imc.rnd.lang.gowasm.ir.op.RelOp;
import com.imc.rnd.lang.gowasm.ir.val.Arg;
import com.imc.rnd.lang.gowasm.ir.val.Int;
import com.imc.rnd.lang.gowasm.ir.val.Local;
import com.imc.rnd.lang.gowasm.ir.val.Temp;

import java.util.List;
import java.util.stream.Collectors;

public class IRDemo {
    public static void main(String[] args) {
        LocalIRBuffer codeBuffer = new LocalIRBuffer();

        codeBuffer.addOperation(new LocalDef("a", new Int(0)));
        codeBuffer.addOperation(new Move(new Local("a"), new Int(10)));

        codeBuffer.addOperation(new Comparison(RelOp.LT, new Temp(0), new Local("a"), new Int(2)));
        codeBuffer.addOperation(new CondJump(new Temp(0), new Label("b1.false")));

        codeBuffer.addOperation(new Move(new Local("a"), new Int(20)));

        // d = a + b + c
        // t0 = a + b
        codeBuffer.addOperation(
                new ArithmeticCalc(ArithmeticOp.PLUS, new Temp(1), new Arg("n"), new Int(2))
                        .withLabel(new Label("b1.false")));
        // t1 = t0 + c
        codeBuffer.addOperation(new ArithmeticCalc(ArithmeticOp.PLUS, new Temp(2), new Temp(0), new Local("c")));

        // SetArg(0, t0)

        // TODO SetArg(index, val), Call(name), Compare(Val, Val), CondJump
        // TODO temporaries are defined like locals, format: t_<temp_index>
        System.out.println("--- code buffer contents ---");
        System.out.println(codeBuffer.dumpOperationsToString());

        LocalWATCodeEmitter emitter = new LocalWATCodeEmitter(codeBuffer);
        List<String> functionCode = emitter.emit();

        System.out.println("--- compiled code ---");
        System.out.println(functionCode.stream().collect(Collectors.joining("\n")));
    }
}
