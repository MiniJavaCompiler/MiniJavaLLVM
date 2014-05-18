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
import java.util.Arrays;

import compiler.*;
import checker.*;
import syntax.*;

public class LLVM {
    static public enum GlobalFn {
        NEW_OBJECT,
        NEW_ARRAY,
        PUTC,
        GCROOT,
        //        INIT,
    };
    private Builder builder;
    private Module module;
    private Value function;
    //private BasicBlock staticInit;
    //private Value staticInitFn;

    private Hashtable<String, Value> namedValues;
    private Value [] globalFns;

    public LLVM() {
        namedValues = new Hashtable<String, Value>();
        globalFns = new Value[GlobalFn.values().length];
    }

    public Value getGlobalFn(GlobalFn g) {
        return globalFns[g.ordinal()];
    }
    /*
    public BasicBlock getStaticInit() {
        return staticInit;
    }
    */
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


    public void buildGlobalFns(Module mod) {
        TypeRef [] args = {Type.CHAR.llvmType()};
        TypeRef printf_type = TypeRef.functionType(TypeRef.voidType(), false,
                              Arrays.asList(args));

        globalFns[GlobalFn.PUTC.ordinal()] = mod.addFunction("MJC_putc", printf_type);

        TypeRef [] malloc_args = {TypeRef.int32Type()};
        TypeRef malloc_type = TypeRef.functionType(TypeRef.int8Type().pointerType(),
                              malloc_args);

        globalFns[GlobalFn.NEW_OBJECT.ordinal()] = mod.addFunction("MJC_allocObject",
                malloc_type);

        TypeRef [] malloc_array_args = {TypeRef.int32Type(), TypeRef.int32Type()};
        TypeRef malloc_array_type = TypeRef.functionType(
                                        TypeRef.int8Type().pointerType(),
                                        malloc_array_args);
        globalFns[GlobalFn.NEW_ARRAY.ordinal()] = mod.addFunction("MJC_allocArray",
                malloc_array_type);


        TypeRef [] gcroot_args = {TypeRef.int8Type().pointerType().pointerType(), TypeRef.int8Type().pointerType()};
        TypeRef gcroot_type = TypeRef.functionType(TypeRef.voidType(), gcroot_args);
        globalFns[GlobalFn.GCROOT.ordinal()] = mod.addFunction("llvm.gcroot",
        		gcroot_type);        
       
        
        TypeRef program_entry_type = TypeRef.functionType(Type.VOID.llvmType(),
                                     (List)Collections.emptyList());

        /*
        globalFns[GlobalFn.INIT.ordinal()] = mod.addFunction("static_init",
                                             program_entry_type);
        staticInit = getGlobalFn(GlobalFn.INIT).appendBasicBlock("entry");
        */
    }
    public void llvmGen(ClassType [] classes, StringLiteral [] strings,
                        String output_path, Boolean dump) {
        Module mod = Module.createWithName("llvm_module");
        setModule(mod);
        buildGlobalFns(mod);

        TypeRef main_entry_type = TypeRef.functionType(Type.INT.llvmType(),
                                  (List)Collections.emptyList());


        ClassType string = null;
        ClassType char_arr = null;
        for (ClassType c : classes) {
            c.llvmGenTypes(this);
            String name = c.getId().getName();
            if (name.equals("String")) {
                string = c;
            } else if (name.equals("char[]")) {
                char_arr = c;
            }
        }

        Builder builder = Builder.createBuilderInContext(Context.getModuleContext(mod));
        setBuilder(builder);

        int strliteral = 0;
        for (StringLiteral l : strings) {
            org.llvm.Value lit = mod.addGlobal(Type.CHAR.llvmType().arrayType(
                                                   l.getString().length() + 1), "#litstr" + strliteral);
            lit.setInitializer(Value.constString(l.getString()));
            org.llvm.Value [] indices = {Type.INT.llvmType().constInt(0, false), Type.INT.llvmType().constInt(0, false)};
            org.llvm.Value lit_ptr = builder.buildInBoundsGEP(lit, "format", indices);
            org.llvm.Value v = mod.addGlobal(char_arr.llvmType(), "#chr" + strliteral);
            Hashtable<String, org.llvm.Value> args = new
            Hashtable<String, org.llvm.Value>();
            args.put("array", lit);
            args.put("length", Type.INT.llvmType().constInt(l.getString().length(), false));
            v.setInitializer(char_arr.globalInitValue(this, args));

            org.llvm.Value str = mod.addGlobal(string.llvmType(),
                                               "#str" + strliteral);

            Hashtable<String, org.llvm.Value> str_args = new
            Hashtable<String, org.llvm.Value>();
            str_args.put("string", v);

            str.setInitializer(string.globalInitValue(this, str_args));
            strliteral++;
            l.setLLVMString(str);
        }

        for (ClassType c : classes) {
            c.llvmGen(this);
        }
        /*
        builder.positionBuilderAtEnd(staticInit);
        builder.buildRetVoid();

        Value main = mod.addFunction("main", main_entry_type);
        BasicBlock main_block = main.appendBasicBlock("entry");

        builder.positionBuilderAtEnd(main_block);
        builder.buildCall(getGlobalFn(GlobalFn.INIT), "",
                          (List)Collections.emptyList());
        Value userMain = null;
        Boolean found = false;
        for (ClassType c : classes) {
            if (c.getMethods() != null) {
                for (MethEnv m : c.getMethods()) {
                    if (m.isMain()) {
                        userMain = m.getFunctionVal(this);
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
        */

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
