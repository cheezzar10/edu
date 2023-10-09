package com.imc.rnd.lang.gowasm.ir;

import com.imc.rnd.lang.gowasm.ir.op.ArithmeticCalc;
import com.imc.rnd.lang.gowasm.ir.op.Comparison;
import com.imc.rnd.lang.gowasm.ir.op.CondJump;
import com.imc.rnd.lang.gowasm.ir.op.Jump;
import com.imc.rnd.lang.gowasm.ir.op.Label;
import com.imc.rnd.lang.gowasm.ir.op.LocalDef;
import com.imc.rnd.lang.gowasm.ir.op.ArithmeticOp;
import com.imc.rnd.lang.gowasm.ir.op.Move;
import com.imc.rnd.lang.gowasm.ir.op.Nop;
import com.imc.rnd.lang.gowasm.ir.op.RelOp;
import com.imc.rnd.lang.gowasm.ir.op.ReturnVal;
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

        // codeBuffer.addOperation(new Comparison(RelOp.LT, new Temp(0), new Local("a"), new Int(2)));
        var condFalseLabel = new Label("b1.f");
        codeBuffer.addOperation(new CondJump(new Local("a"), RelOp.GE, new Int(2), condFalseLabel));
        codeBuffer.addOperation(new ReturnVal(new Arg("n")));
        codeBuffer.addOperation(condFalseLabel);

        codeBuffer.addOperation(new Move(new Local("a"), new Int(20)));

        codeBuffer.addOperation(new ArithmeticCalc(new Temp(0), new Arg("n"), ArithmeticOp.MINUS, new Int(1)));

        // for {}
        // TODO should be compiled as (loop $l1)
        var loopLabel = new Label("l1");
        codeBuffer.addOperation(loopLabel);
        codeBuffer.addOperation(new Nop());
        codeBuffer.addOperation(new Jump(loopLabel));

        // SetArg(0, t0)

        // TODO CallFunc(var, name, params list)
        // TODO temporaries are defined like locals, format: t.<temp_index>
        System.out.println("--- code buffer contents ---");
        System.out.println(codeBuffer.dumpOperationsToString());

        LocalWATCodeEmitter emitter = new LocalWATCodeEmitter(codeBuffer);
        List<String> functionCode = emitter.emit();

        System.out.println("--- compiled code ---");
        System.out.println(functionCode.stream().collect(Collectors.joining("\n")));
    }
}
