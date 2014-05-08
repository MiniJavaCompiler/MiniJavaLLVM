package codegen;
import syntax.*;
import org.llvm.Value;
import org.llvm.BasicBlock;
import org.llvm.Builder;
import org.llvm.ExecutionEngine;
import org.llvm.GenericValue;
import org.llvm.LLVMException;
import org.llvm.Module;
import org.llvm.PassManager;
import org.llvm.Target;
import org.llvm.TypeRef;
import org.llvm.Value;

import org.llvm.binding.LLVMLibrary.LLVMCallConv;
import org.llvm.binding.LLVMLibrary.LLVMIntPredicate;


import compiler.*;

public class LLVM {
    private Builder builder;
    public LLVM() {
    }

    public void setBuilder(Builder b) {
        builder = b;
    }

    public Builder getBuilder() {
        return builder;
    }
    public void llvmGen(ClassType [] classes) {
        for (ClassType c : classes) {
            System.out.print("x\n");
            c.llvmGen(this);
        }
        /*
        SourcePosition p = new SourcePosition(new JavaSource("some file", null), 0, 0);
        Expression expr = new MulExpr(p, new IntLiteral(p, 2), new IntLiteral(p, 3));
        Module mod = Module.createWithName("fac_module");
        Value fac = mod.addFunction("fac",
                                    TypeRef.functionType(TypeRef.int32Type()));
        fac.setFunctionCallConv(LLVMCallConv.LLVMCCallConv);

        BasicBlock entry = fac.appendBasicBlock("entry");
        Builder builder = Builder.createBuilder();
        builder.positionBuilderAtEnd(entry);
        Value v = expr.llvmGen(this);
        //builder.buildRet(v);

        */
    }
}
