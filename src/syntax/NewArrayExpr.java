// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;
import org.llvm.Builder;
import org.llvm.TypeRef;

/** Provides a representation for "new" expressions that allocate an
 *  instance of a class.
 */
public final class NewArrayExpr extends StatementExpr {
    private Type elementType;
    private Name name;
    private ClassType  cls;
    private Expression size;

    public NewArrayExpr(Position pos, Type elementType, Expression size) {
        super(pos);
        this.elementType = elementType;
        this.name = new Name(new Id(pos, elementType.toString() + "[]"));
        this.size = size;
        this.cls  = null;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        if (size.typeOf(ctxt, env) != Type.INT) {
            throw new Failure(pos, "Array size must be of Type INT");
        }
        cls = name.asClass(ctxt);
        if (cls == null) {
            throw new Failure(pos, "Undefined name " + name);
        }
        return cls;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        throw new RuntimeException("Unimplemented");
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        throw new RuntimeException("Unimplemented");
    }

    public org.llvm.Value llvmGen(LLVM l) {
        Builder b = l.getBuilder();
        org.llvm.Value [] obj_args = {cls.llvmType().sizeOf()};
        org.llvm.Value mem = b.buildCall(l.getGlobalFn(LLVM.GlobalFn.NEW_OBJECT),
                                         "new_obj", obj_args);
        org.llvm.Value class_obj = b.buildBitCast(mem, cls.llvmType().pointerType(),
                                   "cast");

        org.llvm.Value arr_len = size.llvmGen(l);
        org.llvm.Value element_size = b.buildTruncOrBitCast(cls.llvmType().sizeOf(),
                                      TypeRef.int32Type(), "size");
        org.llvm.Value mult = b.buildMul(arr_len, element_size, "mul");
        org.llvm.Value [] args = {mult};
        org.llvm.Value array = b.buildCall(l.getGlobalFn(LLVM.GlobalFn.NEW_ARRAY),
                                           "new_arr", obj_args);

        org.llvm.Value vtable = b.buildStructGEP(class_obj, 0, "vtable");
        b.buildStore(cls.getVtableLoc(), vtable);

        org.llvm.Value len = b.buildStructGEP(class_obj,
                                              cls.findField("length").getFieldIndex(), "length");
        b.buildStore(arr_len, len);

        org.llvm.Value actual_arr = b.buildStructGEP(class_obj,
                                    cls.findField("array").getFieldIndex(), "array");
        b.buildStore(array, actual_arr);

        return class_obj;
    }
}
