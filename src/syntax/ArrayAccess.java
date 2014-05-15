// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Represents an access to an instance field.
 */
public final class ArrayAccess extends FieldAccess {
    private Expression object;
    private Expression index;
    private ArrayType array_class;
    public ArrayAccess(Position pos, Expression object, Expression index) {
        super(pos);
        this.object = object;
        this.index = index;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        Type receiver = object.typeOf(ctxt, env);
        ArrayType cls = array_class = receiver.isArray();
        Type index_type = index.typeOf(ctxt, env);
        if (cls == null) {
            throw new Failure(pos,
            "Cannot do an array access on non-array type");
        } else if (!index_type.equal(Type.INT)) {
            throw new Failure(pos,
            "Index type for array must be int (not " + index_type + ")");
        }
        return cls.getElementType();
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        throw new RuntimeException("Unimplemented");
    }

    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    void saveVar(Assembly a, int free) {
        throw new RuntimeException("Unimplemented");
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        throw new RuntimeException("Unimplemented");
    }

    /** Save a value in the location specified by this left hand side.
     */
    public void save(State st, Value val) {
        throw new RuntimeException("Unimplemented");
    }

    public org.llvm.Value llvmGen(LLVM l) {
        org.llvm.Value array_addr = l.getBuilder().buildStructGEP(
                                        object.llvmGen(l),
                                        array_class.findField("array").getFieldIndex(), "array");
        org.llvm.Value array = l.getBuilder().buildLoad(array_addr, "array");
        org.llvm.Value cast = l.getBuilder().buildBitCast(array,
                              array_class.getElementType().llvmTypeField().pointerType(), "cast");
        org.llvm.Value [] indices = {index.llvmGen(l)};
        org.llvm.Value elem_addr = l.getBuilder().buildInBoundsGEP(cast, "array",
                                   indices);
        return l.getBuilder().buildLoad(elem_addr, "elem");
    }

    public org.llvm.Value llvmSave(LLVM l, org.llvm.Value r) {
        org.llvm.Value array_addr = l.getBuilder().buildStructGEP(
                                        object.llvmGen(l),
                                        array_class.findField("array").getFieldIndex(), "array");
        org.llvm.Value array = l.getBuilder().buildLoad(array_addr, "array");
        org.llvm.Value cast = l.getBuilder().buildBitCast(array,
                              array_class.getElementType().llvmTypeField().pointerType(), "cast");
        org.llvm.Value [] indices = {index.llvmGen(l)};
        org.llvm.Value elem_addr = l.getBuilder().buildInBoundsGEP(cast, "array",
                                   indices);
        l.getBuilder().buildStore(r, elem_addr);
        return r;
    }
}
