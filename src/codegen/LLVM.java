package codegen;
import syntax.*;
import org.llvm.Value;
import org.llvm.BasicBlock;
import org.llvm.Builder;
import org.llvm.ExecutionEngine;
import org.llvm.GenericValue;
import org.llvm.LLVMException;
import org.llvm.Module;
import org.llvm.Context;
import org.llvm.PassManager;
import org.llvm.Target;
import org.llvm.TypeRef;
import org.llvm.Value;

import org.llvm.binding.LLVMLibrary.LLVMCallConv;
import org.llvm.binding.LLVMLibrary.LLVMIntPredicate;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import compiler.*;

public class LLVM {
    private Builder builder;
    private Module module;
    private Hashtable<String, Value> namedValues;
    org.llvm.Value programEntry;
    BasicBlock programEntryBlock;
    org.llvm.Value userEntry;

    public BasicBlock getProgramEntry() {
        return programEntryBlock;
    }
    public void setUserEntry(org.llvm.Value userEntryPoint) {
        userEntry = userEntryPoint;
    }
    public LLVM() {
        namedValues = new Hashtable<String, Value>();
    }

    public Value getNamedValue(String s) {
        Value v = namedValues.get(s);
        //v = builder.buildLoad(v, s);
        return v;
    }

    public void setNamedValue(String s, Value v) {
        v.setValueName(s);
        namedValues.put(s, v);
    }
    public void setBuilder(Builder b) {
        builder = b;
    }

    public Builder getBuilder() {
        return builder;
    }

    public Module getModule() {
        return module;
    }
    public void setModule(Module module) {
        this.module = module;
    }
    public void llvmGen(ClassType [] classes) {
        Module mod = Module.createWithName("llvm_module");
        Builder builder = Builder.createBuilderInContext(Context.getModuleContext(mod));
        setModule(mod);
        setBuilder(builder);
        TypeRef program_entry_type = TypeRef.functionType(Type.VOID.llvmType(),
                                     (List)Collections.emptyList());
        programEntry = mod.addFunction("prog_entry", program_entry_type);
        programEntryBlock = programEntry.appendBasicBlock("entry");
        for (ClassType c : classes) {
            c.llvmGen(this);
        }
        builder.positionBuilderAtEnd(programEntryBlock);
        builder.buildCall(userEntry, "", (List)Collections.emptyList());
        builder.buildRetVoid();
        mod.dumpModule();
        try {
            mod.verify();
        } catch (LLVMException e) {
            System.out.println(e.getMessage());
        }
    }
}
