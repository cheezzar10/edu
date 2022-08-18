package com.imc.rnd.lang.gowasm.ir;

import com.imc.rnd.lang.gowasm.ir.op.ArithmeticCalc;
import com.imc.rnd.lang.gowasm.ir.op.ArithmeticOp;
import com.imc.rnd.lang.gowasm.ir.op.Comparison;
import com.imc.rnd.lang.gowasm.ir.op.CondJump;
import com.imc.rnd.lang.gowasm.ir.op.Label;
import com.imc.rnd.lang.gowasm.ir.op.LocalDef;
import com.imc.rnd.lang.gowasm.ir.op.Move;
import com.imc.rnd.lang.gowasm.ir.op.Op;
import com.imc.rnd.lang.gowasm.ir.op.RelOp;
import com.imc.rnd.lang.gowasm.ir.op.ReturnVal;
import com.imc.rnd.lang.gowasm.ir.val.Arg;
import com.imc.rnd.lang.gowasm.ir.val.Int;
import com.imc.rnd.lang.gowasm.ir.val.Local;
import com.imc.rnd.lang.gowasm.ir.val.NamedVal;
import com.imc.rnd.lang.gowasm.ir.val.Temp;
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
            case LABEL: return emitLabelCode((Label)op);
            case RETURN_VAL: return emitReturnValCode((ReturnVal)op);
            default: throw new IllegalArgumentException("unsupported op code: " + op.getOpCode());
        }
    }

    private Stream<String> emitReturnValCode(ReturnVal returnVal) {
        return Stream.concat(
                emitLoadFromValCode(returnVal.getValue()),
                Stream.of("return"));
    }

    private Stream<String> emitLabelCode(Label label) {
        return Stream.of(") ;; " + label.getName());
    }

    private Stream<String> emitArithmeticExprCode(ArithmeticCalc arithmeticCalc) {
        return Stream.concat(
                Stream.concat(
                        Stream.concat(
                                emitLoadFromValCode(arithmeticCalc.getLeftOperand()),
                                emitLoadFromValCode(arithmeticCalc.getRightOperand())),
                        emitArithmeticOperatorCode(arithmeticCalc.getOperator())),
                emitStoreToVarCode(arithmeticCalc.getTarget()));
    }

    private Stream<String> emitArithmeticOperatorCode(ArithmeticOp operator) {
        switch (operator) {
            case PLUS: return Stream.of("i32.add");
            case MINUS: return Stream.of("i32.sub");
            default: throw new IllegalArgumentException("unsupported arithmetical operator: " + operator);
        }
    }

    private Stream<String> emitComparisonCode(Comparison comparison) {
        Stream<String> leftOperandLoadCode = emitLoadFromValCode(comparison.getLeftOperand());
        Stream<String> rightOperandLoadCode = emitLoadFromValCode(comparison.getRightOperand());
        Stream<String> relOpCode = emitComparisonOperatorCode(comparison.getOperator());
        Stream<String> storeResultCode = emitStoreToVarCode(comparison.getTarget());

        return Stream.concat(
                Stream.concat(
                        Stream.concat(leftOperandLoadCode, rightOperandLoadCode),
                        relOpCode),
                storeResultCode);
    }

    private Stream<String> emitComparisonOperatorCode(RelOp operator) {
        switch (operator) {
            case LT: return Stream.of("i32.lt_s");
            case GE: return Stream.of("i32.ge_s");
            default: throw new IllegalArgumentException("unsupported comparison operator: " + operator);
        }
    }

    private Stream<String> emitCondJumpCode(CondJump condJump) {
        Stream<String> blockDefCode = Stream.of("(block $" + condJump.getTargetLabel().getName());
        Stream<String> leftOperandLoadCode = emitLoadFromValCode(condJump.getLeftOperand());
        Stream<String> rightOperandLoadCode = emitLoadFromValCode(condJump.getRightOperand());
        Stream<String> comparisonCode = emitComparisonOperatorCode(condJump.getOperator());
        Stream<String> conditionalBranchCode = Stream.of("br_if $" + condJump.getTargetLabel().getName());

        return Stream.concat(
                Stream.concat(
                        Stream.concat(
                                Stream.concat(blockDefCode, leftOperandLoadCode),
                                rightOperandLoadCode),
                        comparisonCode),
                conditionalBranchCode);
    }

    private Stream<String> emitMoveCode(Move move) {
        Stream<String> moveSourceCode = emitLoadFromValCode(move.getSource());
        Stream<String> moveTargetCode = emitStoreToVarCode(move.getTarget());

        return Stream.concat(moveSourceCode, moveTargetCode);
    }

    private Stream<String> emitLoadFromValCode(Val value) {
        switch (value.getKind()) {
            case INT: return emitLoadIntLiteralCode((Int)value);
            case LOCAL:
            case TEMP:
            case ARG: return emitLoadFromNamedValCode((NamedVal)value);
            default: throw new IllegalArgumentException("unsupported move source kind: " + value.getKind());
        }
    }

    private Stream<String> emitStoreToVarCode(Var targetVar) {
        switch (targetVar.getKind()) {
            case LOCAL:
            case TEMP: return Stream.of("local.set $" + targetVar.getName());
            default: throw new IllegalArgumentException("unsupported move target kind: " + targetVar.getKind());
        }
    }

    private Stream<String> emitLoadIntLiteralCode(Int intLiteral) {
        return Stream.of("i32.const " + intLiteral.getValue());
    }

    private Stream<String> emitLoadFromNamedValCode(NamedVal value) {
        return Stream.of("local.get $" + value.getName());
    }

    private Stream<String> emitLocalDefCode(LocalDef localDef) {
        String localDefTypeName = "i32"; // TODO get from symbol table
        // TODO also, generate initial value initialization code ( local.set $<var name> )
        return Stream.of("(local $" + localDef.getName() + " " + localDefTypeName + ")");
    }
}
