package com.imc.rnd.lang.gowasm.ir;

import com.imc.rnd.lang.gowasm.ir.op.ArithmeticCalc;
import com.imc.rnd.lang.gowasm.ir.op.Comparison;
import com.imc.rnd.lang.gowasm.ir.op.CondJump;
import com.imc.rnd.lang.gowasm.ir.op.LocalDef;
import com.imc.rnd.lang.gowasm.ir.op.Move;
import com.imc.rnd.lang.gowasm.ir.op.Op;
import com.imc.rnd.lang.gowasm.ir.val.Int;
import com.imc.rnd.lang.gowasm.ir.val.Local;
import com.imc.rnd.lang.gowasm.ir.val.Val;
import com.imc.rnd.lang.gowasm.ir.val.Var;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalWATCodeEmitter {
    private final LocalIRBuffer codeBuffer;

    public LocalWATCodeEmitter(LocalIRBuffer codeBuffer) {
        this.codeBuffer = codeBuffer;
    }

    public List<String> emit() {
        return codeBuffer.getOperations().stream()
                .flatMap(this::emitOperationCode)
                .collect(Collectors.toList());
    }

    private Stream<String> emitOperationCode(Op op) {
        switch (op.getOpCode()) {
            case LOCAL_DEF: return emitLocalDefCode((LocalDef)op);
            case MOVE: return emitMoveCode((Move)op);
            case COND_JUMP: return emitCondJumpCode((CondJump)op);
            case COMPARISON: return emitComparisonCode((Comparison)op);
            case ARITHMETIC_CALC: return emitArithmeticExprCode((ArithmeticCalc)op);
            default: throw new IllegalArgumentException("unsupported op code: " + op.getOpCode());
        }
    }

    private Stream<String> emitArithmeticExprCode(ArithmeticCalc arithmeticExpr) {
        return Stream.empty();
    }

    private Stream<String> emitComparisonCode(Comparison comparison) {
        return Stream.empty();
    }

    private Stream<String> emitCondJumpCode(CondJump condJump) {
        return Stream.empty();
    }

    private Stream<String> emitMoveCode(Move move) {
        Stream<String> moveSourceCode = emitMoveSourceCode(move.getSource());
        Stream<String> moveTargetCode = emitMoveTargetCode(move.getTarget());

        return Stream.concat(moveSourceCode, moveTargetCode);
    }

    private Stream<String> emitMoveSourceCode(Val moveSource) {
        switch (moveSource.getKind()) {
            case INT: return emitIntLiteralCode((Int)moveSource);
            default: throw new IllegalArgumentException("unsupported move source kind: " + moveSource.getKind());
        }
    }

    private Stream<String> emitMoveTargetCode(Var moveTarget) {
        switch (moveTarget.getKind()) {
            case LOCAL: return emitLocalAssignmentCode((Local)moveTarget);
            default: throw new IllegalArgumentException("unsupported move target kind: " + moveTarget.getKind());
        }
    }

    private Stream<String> emitLocalAssignmentCode(Local localVar) {
        return Stream.of(
                "local.set $" +
                        localVar.getName().orElseThrow(
                                () -> new IllegalArgumentException("unnamed local")));
    }

    private Stream<String> emitIntLiteralCode(Int intLiteral) {
        return Stream.of("i32.const " + intLiteral.getValue());
    }

    private Stream<String> emitLocalDefCode(LocalDef localDef) {
        String localDefTypeName = "i32"; // TODO get from symbol table
        // TODO also, generate initial value initialization code ( local.set $<var name> )
        return Stream.of("(local $" + localDef.getName() + " " + localDefTypeName + ")");
    }
}
