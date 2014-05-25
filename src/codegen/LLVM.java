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
import org.llvm.Dwarf;

import org.llvm.binding.LLVMLibrary.LLVMCallConv;
import org.llvm.binding.LLVMLibrary.LLVMIntPredicate;
import org.llvm.binding.LLVMLibrary.LLVMAttribute;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;
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
        DBG_DECLARE,
    };
    private Builder builder;
    private Module module;
    private Value function;

    private Hashtable<String, Value> namedValues;
    private Value [] globalFns;
    private String inputFile;

    private Stack<Value> lexicalScope;
    private int scopeCount;
    private Value filePair;
    private Value llvmContext;
    private Value file;

    private ArrayList<Value> subprograms;
    public LLVM(String inputfile) {
        namedValues = new Hashtable<String, Value>();
        globalFns = new Value[GlobalFn.values().length];
        inputFile = inputfile;
        lexicalScope = new Stack<Value>();
        scopeCount = 0;
        subprograms = new ArrayList<Value>();

        File f = new File(inputFile).getAbsoluteFile();
        this.filePair = Value.MDNode(
        new Value[] {
            Value.MDString(f.getName()),
            Value.MDString(f.getParent())
        });

        this.file = Value.MDNode(
        new Value[] {
            Dwarf.DW_TAG.DW_TAG_file_type.value(),
            this.filePair //Source directory (including trailing slash) & file pair
        });
        enterContext(this.file);
    }

    public Value getGlobalFn(GlobalFn g) {
        return globalFns[g.ordinal()];
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

    public void enterContext(Value v) {
        /* pushing an already created context */
        lexicalScope.push(v);
    }

    public Value getFile() {
        return filePair;
    }
    public void addSubProgram(Value v) {
        subprograms.add(v);
    }
    public void enterContext(Position pos) {
        int tempScope = scopeCount;
        /* lexical block does not match description from webpage, missing a field */
        Value [] compile_unit_data =  {
            Dwarf.DW_TAG.DW_TAG_lexical_block.value(), //Tag = 11 (DW_TAG_lexical_block)
            filePair, //Source directory (including trailing slash) & file pair
            lexicalScope.peek(), //Reference to context descriptor
            TypeRef.int32Type().constInt(pos.getRow(), false),      //Line number
            TypeRef.int32Type().constInt(pos.getColumn(), false),     //Column number
            //TypeRef.int32Type().constInt(0, false),     // DWARF path discriminator value
            TypeRef.int32Type().constInt(tempScope, false),      // Unique ID to identify blocks from a template function
        };
        lexicalScope.push(Value.MDNode(compile_unit_data));
        scopeCount++;
    }
    public Value getMetaContext() {
        return lexicalScope.peek();
    }
    public void exitContext() {
        lexicalScope.pop();
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

        globalFns[GlobalFn.GCROOT.ordinal()].addFunctionAttr(
            LLVMAttribute.LLVMNoUnwindAttribute);

        TypeRef metadata = Value.MDNode(new Value[] {TypeRef.int32Type().constNull()}).typeOf();
        TypeRef [] debug_declare_args = {metadata, metadata};
        TypeRef debug_declare_type = TypeRef.functionType(TypeRef.voidType(),
                                     debug_declare_args);


        globalFns[GlobalFn.DBG_DECLARE.ordinal()] = mod.addFunction("llvm.dbg.declare",
                debug_declare_type);

        globalFns[GlobalFn.DBG_DECLARE.ordinal()].addFunctionAttr(
            LLVMAttribute.LLVMNoUnwindAttribute);

        TypeRef program_entry_type = TypeRef.functionType(Type.VOID.llvmType(),
                                     (List)Collections.emptyList());
    }
    public Value setLLVMMetaData(Position pos, Value instr) {
        Value meta = Value.MDNode(
        new Value[] {
            TypeRef.int32Type().constInt(pos.getRow(), false),
            TypeRef.int32Type().constInt(pos.getColumn(), false),
            getMetaContext(),
            new org.llvm.Value(null),
        });
        instr.setMetadata(0 /*kind ? */, meta);
        return instr;
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
    public void compileUnitDescriptor(Module mod) {
        //Value empty = Value.MDNode(new Value[]{TypeRef.int32Type().constInt(0, false)});
        Value empty = new Value(null);
        Value [] compile_unit_data =  {
            Dwarf.DW_TAG.DW_TAG_compile_unit.value(),
            filePair,        // Source directory (including trailing slash) & file pair
            Dwarf.DW_LANG.DW_LANG_Java.value(),      // DWARF language identifier (ex. DW_LANG_C89)
            Value.MDString("MiniJava Compiler"),     // Producer (ex. "4.0.1 LLVM (LLVM research group)")
            TypeRef.int1Type().constInt(0, false),   // True if this is optimized.
            Value.MDString(""),                      // Flags
            TypeRef.int32Type().constInt(0, false),  // Runtime version
            empty,    // List of enums types
            empty,    // List of retained types
            Value.MDNode(subprograms.toArray(new Value[0])),    // List of subprograms
            empty,    // List of global variables
            empty,    // List of imported entities
            Value.MDString("")                  // Split debug filename
        };
        mod.addNamedMetaData("llvm.dbg.cu", Value.MDNode(compile_unit_data));
        mod.addNamedMetaData("llvm.module.flags", Value.MDNode(
                                 new Value[] {TypeRef.int32Type().constInt(2, false),
                                              Value.MDString("Dwarf Version"),
                                              TypeRef.int32Type().constInt(4, false)
                                             }));
        mod.addNamedMetaData("llvm.module.flags", Value.MDNode(
                                 new Value[] {TypeRef.int32Type().constInt(1, false),
                                              Value.MDString("Debug Info Version"),
                                              TypeRef.int32Type().constInt(1, false)
                                             }));

    }

    public void localVar(Position pos, int arg_num, VarDecls v, Value val) {
        /*
        Value tag;
        Value line_info;
        if (arg_num > 0) {
            tag = Dwarf.DW_TAG.DW_TAG_arg_variable.value();
            // 24 bit - Line number where defined
            // 8 bit - Argument number. 1 indicates 1st argument.
            line_info = TypeRef.int32Type().constInt(v.getId().getPos().getRow() << 8 |
                        arg_num, false);
        } else {
            tag = Dwarf.DW_TAG.DW_TAG_auto_variable.value();
            line_info = TypeRef.int32Type().constInt(v.getId().getPos().getRow(), false);
        }
        Value node = Value.MDNode(new Value[] {
            tag,      // Tag
            lexicalScope.peek(), // Context
            Value.MDString(v.getId().getName()), // Name
            filePair, // Reference to file where defined
            line_info,
            new org.llvm.Value(null), //Type.INT.llvmMetaData(), // Reference to the type descriptor
            TypeRef.int32Type().constInt(0, false),      // flags
            new org.llvm.Value(null),
        });
        Value [] args = new Value[] {
            Value.MDNode(new Value[]{val}), node
        };
        Value dbg = getBuilder().buildCall(getGlobalFn(LLVM.GlobalFn.DBG_DECLARE), "",
                                           args);
        setLLVMMetaData(pos, dbg);
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
            args.put("array", lit_ptr);
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

        //add statics to gcroot
        org.llvm.Value static_gcroots = mod.addFunction("MJCStatic_roots",
                                        TypeRef.functionType(Type.VOID.llvmType(), (List)Collections.emptyList()));
        BasicBlock entry = static_gcroots.appendBasicBlock("entry");
        builder.positionBuilderAtEnd(entry);
        org.llvm.Value md_null = new org.llvm.Value(null);
        org.llvm.Value func_type = org.llvm.Value.MDNode(
        new org.llvm.Value[] {
            Dwarf.DW_TAG.DW_TAG_subroutine_type.value(),     // Tag (see below)
            md_null,      // Source directory (including trailing slash) & file pair (may be null)
            md_null,      // Reference to Context
            org.llvm.Value.MDString(""),        // Name (may be "" for anonymous types)
            TypeRef.int32Type().constInt(0, false),      // Line number where defined (may be 0)
            TypeRef.int64Type().constInt(0, false),      // Size in bits
            TypeRef.int64Type().constInt(0, false),      // Alignment in bits
            TypeRef.int64Type().constInt(0, false),      // Offset in bits
            TypeRef.int32Type().constInt(0, false),      // Flags
            md_null, // Reference to type derived from
            //org.llvm.Value.MDNode(args.toArray(new org.llvm.Value[0])), // Reference to array of member descriptors
            TypeRef.int32Type().constInt(0, false), // Runtime languages
            md_null, // Base type containing the vtable pointer for this type
            md_null, // Template parameters
            md_null   // A unique identifier for type uniquing purpose (may be null)
        });
        org.llvm.Value md = org.llvm.Value.MDNode(
        new org.llvm.Value[] {
            Dwarf.DW_TAG.DW_TAG_subprogram.value(), // Tag = 46 (DW_TAG_subprogram)
            getFile(), // Source directory (including trailing slash) & file pair
            getMetaContext(), // Reference to context descriptor
            org.llvm.Value.MDString("staticroots"), //Name
            org.llvm.Value.MDString("staticroots"), // Display name (fully qualified C++ name)
            org.llvm.Value.MDString("staticroots"), // MIPS linkage name (for C++)
            TypeRef.int32Type().constInt(0, false),      // Line number where defined
            func_type, // Reference to type descriptor
            TypeRef.int1Type().constInt(/*isStatic() ? 1 :*/ 0, false), // True if the global is local to compile unit (static)
            TypeRef.int1Type().constInt(1, false),       //True if the global is defined in the compile unit (not extern)
            TypeRef.int32Type().constInt(0, false),      // Virtuality, e.g. dwarf::DW_VIRTUALITY__virtual
            TypeRef.int32Type().constInt(0, false),      // Index into a virtual function
            md_null, // indicates which base type contains the vtable pointer for the
            // derived class
            TypeRef.int32Type().constInt(0, false),      // Flags - Artificial, Private, Protected, Explicit, Prototyped.
            TypeRef.int1Type().constInt(0, false),       // isOptimized
            static_gcroots , // Pointer to LLVM function
            new org.llvm.Value(null), // Lists function template parameters
            new org.llvm.Value(null), // Function declaration descriptor
            new org.llvm.Value(null), // List of function variables
            TypeRef.int32Type().constInt(0, false) // Line number where the scope of the subprogram begins
        });
        enterContext(md);
        addSubProgram(md);

        for (ClassType c : classes) {
            if (c.getFields() != null) {
                for (FieldEnv f : c.getFields()) {
                    if (f.isStatic()) {
                        org.llvm.Value v = f.getStaticField();
                        Type type = f.getType();
                        // CALL SOME GLOBAL ROOT REGISTRATION
                        // LLVM explicitly states they don't handle global roots
                        // with this method.
                    }
                }
            }
        }
        builder.buildRetVoid();

        for (ClassType c : classes) {
            c.llvmGen(this);
        }

        compileUnitDescriptor(mod);

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
