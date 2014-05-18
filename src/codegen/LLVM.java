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
import java.util.ArrayList;

import compiler.*;
import checker.*;
import syntax.*;

public class LLVM {
    private Builder builder;
    private Module module;
    private Value function;
    private BasicBlock staticInit;
    private Value staticInitFn;
    private Value printf;
    private Value malloc;
    private Value gcroot;

    private Hashtable<String, Value> namedValues;

    public Value getStaticInitFn() {
        return staticInitFn;
    }
    public BasicBlock getStaticInit() {
        return staticInit;
    }

    public LLVM() {
        namedValues = new Hashtable<String, Value>();
    }

    public Value getNamedValue(String s) {
        Value v = namedValues.get(s);
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

    public void setFunction(Value f) {
        this.function = f;
    }

    public Value getFunction() {
        return function;
    }

    public Module getModule() {
        return module;
    }
    public void setModule(Module module) {
        this.module = module;
    }

    public Value getPrintf() {
        return printf;
    }

    public Value getMalloc() {
        return malloc;
    }
    
    public Value getGCRoot() {
    	return gcroot;
    }

    public void llvmGen(ClassType [] classes, String output_path, Boolean dump) {
        Module mod = Module.createWithName("llvm_module");
        setModule(mod);
        TypeRef [] args = {TypeRef.int8Type().pointerType(), TypeRef.int32Type()};
        TypeRef printf_type = TypeRef.functionType(TypeRef.int32Type(), args);
        printf = mod.addFunction("printf", printf_type);
        printf.setFunctionCallConv(LLVMCallConv.LLVMCCallConv);

        TypeRef [] malloc_args = {TypeRef.int64Type()};
        TypeRef malloc_type = TypeRef.functionType(TypeRef.int8Type().pointerType(),
                              malloc_args);
        malloc = mod.addFunction("new_object", malloc_type);
        malloc.setFunctionCallConv(LLVMCallConv.LLVMCCallConv);

        TypeRef [] gcroot_args = {TypeRef.int8Type().pointerType().pointerType(), TypeRef.int8Type().pointerType()};
        TypeRef gcroot_type = TypeRef.functionType(TypeRef.voidType(), gcroot_args);
        gcroot = mod.addFunction("llvm.gcroot", gcroot_type);
        gcroot.setFunctionCallConv(LLVMCallConv.LLVMCCallConv);
        
        
        TypeRef program_entry_type = TypeRef.functionType(Type.VOID.llvmType(),
                                     (List)Collections.emptyList());

        staticInitFn = mod.addFunction("static_init", program_entry_type);
        staticInit = staticInitFn.appendBasicBlock("entry");

        TypeRef main_entry_type = TypeRef.functionType(Type.INT.llvmType(),
                                  (List)Collections.emptyList());


        for (ClassType c : classes) {
            c.llvmGenTypes(this);
        }
        Builder builder = Builder.createBuilderInContext(Context.getModuleContext(mod));
        setBuilder(builder);

        for (ClassType c : classes) {
            c.llvmGen(this);
        }

        builder.positionBuilderAtEnd(staticInit);
        builder.buildRetVoid();

        Value main = mod.addFunction("main", main_entry_type);
        BasicBlock main_block = main.appendBasicBlock("entry");

        builder.positionBuilderAtEnd(main_block);
        builder.buildCall(getStaticInitFn(), "", (List)Collections.emptyList());
        Value userMain = null;
        Boolean found = false;
        for (ClassType c : classes) {
        	if (null != c.getMethods()) {
	            for (MethEnv m : c.getMethods()) {
	                if (m.isMain()) {
	                    userMain = m.getFunctionVal();
	                    found = true;
	                }
	            }
        	}
        }
        if (found) {
            builder.buildCall(userMain, "", (List)Collections.emptyList());
        } else {
            System.out.println("Cannot find user main function");
        }
        builder.buildRet(Type.INT.llvmType().constInt(0, false));

        try {
            if (dump) {
                mod.dumpModule();
            }
            mod.verify();
            if (output_path != null) {
                System.out.println("Writing LLVM Bitcode to " + output_path);
                mod.writeBitcodeToFile(output_path);
            }
        } catch (LLVMException e) {
            System.out.println(e.getMessage());
        }
    }
}
