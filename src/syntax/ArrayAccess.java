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
    private boolean check_enabled;
    private Expression array_check;
    public ArrayAccess(Position pos, Expression object, Expression index,
                       boolean check_enabled) {
        super(pos);
        this.object = object;
        this.index = index;
        this.array_check = null;
        this.check_enabled = check_enabled;
        if (check_enabled) {
            array_check = new NameInvocation(
                new Name(new Name(new Id(pos, "Array")), new Id(pos, "boundsCheck")),
                new Args(new StringLiteral(pos, pos.getFilename()),
                         new Args(new IntLiteral(pos, pos.getRow()),
                                  new Args(
                                      new ObjectAccess(object, new Id(pos, "length")), new Args(index, null)))));
        } else {
            array_check = new IntLiteral(pos, 0); // No Op
        }
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
        array_check.typeOf(ctxt, env);
        return cls.getElementType();
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        array_check.compileExpr(a, free + 1);
        index.compileExpr(a, free);
        a.emit("imull",  a.immed(array_class.getElementType().getWidth()), a.reg(free));
        a.spill(free + 1);
        object.compileExpr(a, free + 1);
        FieldEnv f = array_class.findField("array");
        a.emit("movl", a.indirect(f.getOffset(), a.reg(free + 1)), a.reg(free + 1));
        a.emit("addl", a.reg(free), a.reg(free + 1));
        a.emit("movl", a.indirect(0, a.reg(free + 1)), a.reg(free));
        a.unspill(free + 1);
    }

    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    void saveVar(Assembly a, int free) {
        array_check.compileExpr(a, free + 1);
        a.spill(free + 1);
        index.compileExpr(a, free + 1);
        a.emit("imull", a.immed(array_class.getElementType().getWidth()),
               a.reg(free + 1));
        a.spill(free + 2);
        object.compileExpr(a, free + 2);
        FieldEnv f = array_class.findField("array");
        a.emit("movl", a.indirect(f.getOffset(), a.reg(free + 2)), a.reg(free + 2));
        a.emit("addl", a.reg(free + 1), a.reg(free + 2));
        a.emit("movl", a.reg(free), a.indirect(0, a.reg(free + 2)));
        a.unspill(free + 1);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        array_check.eval(st);
        return object.eval(st).getObj()
               .getField(array_class.findField("array").getOffset())
               .getArray().getElem(index.eval(st).getInt());
    }

    /** Save a value in the location specified by this left hand side.
     */
    public void save(State st, Value val) {
        array_check.eval(st);
        object.eval(st).getObj()
        .getField(array_class.findField("array").getOffset())
        .getArray().setElem(index.eval(st).getInt(), val);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        array_check.llvmGen(l);
        org.llvm.Value array_addr = l.getBuilder().buildStructGEP(
                                        object.llvmGen(l),
                                        array_class.findField("array").getFieldIndex(), "array");
        org.llvm.Value array = l.getBuilder().buildLoad(array_addr, "array");
        org.llvm.Value [] indices = {array, index.llvmGen(l)};
        org.llvm.Value elem_addr = l.getBuilder().buildCall(l.getGlobalFn(
                                       LLVM.GlobalFn.ARRAY_INDEX), "addr", indices);
        org.llvm.Value elem = l.getBuilder().buildBitCast(elem_addr,
                              array_class.getElementType().llvmTypeField().pointerType(), "elem");
        return l.getBuilder().buildLoad(elem, "elem");
    }

    public org.llvm.Value llvmSave(LLVM l, org.llvm.Value r) {
        array_check.llvmGen(l);
        org.llvm.Value array_addr = l.getBuilder().buildStructGEP(
                                        object.llvmGen(l),
                                        array_class.findField("array").getFieldIndex(), "array");
        org.llvm.Value array = l.getBuilder().buildLoad(array_addr, "array");
        org.llvm.Value [] indices = {array, index.llvmGen(l)};
        org.llvm.Value elem_addr = l.getBuilder().buildCall(l.getGlobalFn(
                                       LLVM.GlobalFn.ARRAY_INDEX), "addr", indices);
        org.llvm.Value elem = l.getBuilder().buildBitCast(elem_addr,
                              array_class.getElementType().llvmTypeField().pointerType(), "elem");

        l.getBuilder().buildStore(r, elem);
        return r;
    }
}
