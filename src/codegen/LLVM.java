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
import org.llvm.binding.LLVMLibrary.LLVMAttribute;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import compiler.*;
import checker.*;
import syntax.*;

public class LLVM {
    static public enum GlobalFn {
        NEW_OBJECT,
        NEW_ARRAY,
        PUTC,
        GCROOT,
        GCGBLROOT,
        ARRAY_INDEX,
    };
    class LocalStackVar {
        private Value v;
        private String name;
        private int depth;
        private TypeRef t;
        private boolean needsCleanup;
        public LocalStackVar(int depth, boolean needsCleanup, TypeRef t, String name,
                             Value v) {
            this.t = t;
            this.v = v;
            this.name = name;
            this.depth = depth;
            this.needsCleanup = needsCleanup;
        }
        public void cleanUp(LLVM l) {
            if (needsCleanup) {
                getBuilder().buildStore(t.constNull(), v);
            }
        }
        public Value getValue() {
            return v;
        }
        public int getDepth() {
            return depth;
        }
        public String getName() {
            return name;
        }
        public TypeRef getType() {
            return t;
        }
    }

    private Builder builder;
    private Module module;
    private Value function;

    private Stack<Hashtable<String, LocalStackVar>> localVars;
    private Value [] globalFns;

    public LLVM() {
        localVars = new Stack<Hashtable<String, LocalStackVar>>();
        globalFns = new Value[GlobalFn.values().length];
    }

    public Value getGlobalFn(GlobalFn g) {
        return globalFns[g.ordinal()];
    }
    public Value getNamedValue(String s) {
        Hashtable<String, LocalStackVar> locals = localVars.peek();
        LocalStackVar local = locals.get(s);
        return local.getValue();
    }
    public void setNamedValue(boolean needsCleanup, TypeRef t, String s, Value v) {
        Hashtable<String, LocalStackVar> locals = localVars.peek();
        v.setValueName(s);
        locals.put(s, new LocalStackVar(localVars.size(), needsCleanup, t, s, v));
    }
    public void enterScope() {
        Hashtable<String, LocalStackVar> previous = null;
        Hashtable<String, LocalStackVar> current = new
        Hashtable<String, LocalStackVar>();
        if (localVars.size() > 0) {
            previous = localVars.peek();
        }
        localVars.push(current);
        if (previous != null) {
            for (LocalStackVar p : previous.values()) {
                current.put(p.getName(), p);
            }
        }
    }
    public void leaveScope() {
        for (LocalStackVar p : localVars.peek().values()) {
            if (p.getDepth() >= localVars.size()) {
                p.cleanUp(this);
            }
        }
        localVars.pop();
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


        TypeRef [] gcglobalroot_args = {Type.PTR.llvmType()};
        TypeRef gcglobalroot_type = TypeRef.functionType(TypeRef.voidType(),
                                    gcglobalroot_args);
        globalFns[GlobalFn.GCGBLROOT.ordinal()] = mod.addFunction("MJC_globalRoot",
                gcglobalroot_type);

        TypeRef [] index_args = {Type.PTR.llvmType(), Type.INT.llvmType()};
        TypeRef index_type = TypeRef.functionType(Type.PTR.llvmType(), index_args);
        globalFns[GlobalFn.ARRAY_INDEX.ordinal()] = mod.addFunction("MJC_arrayIndex",
                index_type);

        TypeRef [] gcroot_args = {Type.PTR.llvmType().pointerType(), Type.PTR.llvmType()};
        TypeRef gcroot_type = TypeRef.functionType(TypeRef.voidType(), gcroot_args);
        globalFns[GlobalFn.GCROOT.ordinal()] = mod.addFunction("llvm.gcroot",
                                               gcroot_type);

        TypeRef program_entry_type = TypeRef.functionType(Type.VOID.llvmType(),
                                     (List)Collections.emptyList());
    }

    public void markGCRoot(Value v, Type type) {
        Builder b = getBuilder();
        if (type.isClass() != null || type.isArray() != null) {
            org.llvm.Value res = b.buildBitCast(v,
                                                TypeRef.int8Type().pointerType().pointerType(), "gctmp");
            org.llvm.Value meta =
                TypeRef.int8Type().pointerType().constNull();  // TODO: replace with type data
            org.llvm.Value [] args = {res, meta};
            org.llvm.Value gc = b.buildCall(getGlobalFn(LLVM.GlobalFn.GCROOT), "", args);
        }
    }

    public void llvmGen(ClassType [] classes, StringLiteral [] strings,
                        String output_path, Boolean dump) {
        Module mod = Module.createWithName("llvm_module");
        setModule(mod);
        buildGlobalFns(mod);
        //Entering a "GLOBAL" Scope, the home of static vars.
        enterScope();
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
        //add statics to gcroot
        org.llvm.Value static_gcroots = mod.addFunction("MJCStatic_roots",
                                        TypeRef.functionType(Type.VOID.llvmType(), (List)Collections.emptyList()));
        BasicBlock entry = static_gcroots.appendBasicBlock("entry");
        builder.positionBuilderAtEnd(entry);

        for (ClassType c : classes) {
            if (c.getFields() != null) {
                for (FieldEnv f : c.getFields()) {
                    if (f.isStatic()) {
                        org.llvm.Value v = f.getStaticField();
                        Type type = f.getType();
                        // CALL SOME GLOBAL ROOT REGISTRATION
                        // LLVM explicitly states they don't handle global roots
                        // with this method.
                        Builder b = getBuilder();
                        org.llvm.Value res = b.buildBitCast(v,
                                                            TypeRef.int8Type().pointerType(), "gcgbltmp");
                        org.llvm.Value [] args = {res};
                        org.llvm.Value gc = b.buildCall(getGlobalFn(LLVM.GlobalFn.GCGBLROOT), "", args);
                    }
                }
            }
        }
        builder.buildRetVoid();

        for (ClassType c : classes) {
            c.llvmGen(this);
        }

        /* Do NOT leave the global scope */
        //leaveScope();
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
