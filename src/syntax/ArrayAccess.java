/*
 * MiniJava Compiler - X86, LLVM Compiler/Interpreter for MiniJava.
 * Copyright (C) 2014, 2008 Mitch Souders, Mark A. Smith, Mark P. Jones
 *
 * MiniJava Compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * MiniJava Compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MiniJava Compiler; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


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
    private Expression array_check;
    public ArrayAccess(Position pos, Expression object, Expression index,
                       boolean check_enabled) {
        super(pos);
        this.object = object;
        this.index = index;
        this.array_check = null;
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
        org.llvm.Value array_cast = l.getBuilder().buildBitCast(array,
                                    array_class.getElementType().llvmTypeField().pointerType(), "array_cast");
        org.llvm.Value [] indices = {index.llvmGen(l)};
        org.llvm.Value elem = l.getBuilder().buildInBoundsGEP(array_cast, "elem",
                              indices);
        return l.getBuilder().buildLoad(elem, "elem");
    }

    public void llvmSave(LLVM l, org.llvm.Value r) {
        array_check.llvmGen(l);
        org.llvm.Value array_addr = l.getBuilder().buildStructGEP(
                                        object.llvmGen(l),
                                        array_class.findField("array").getFieldIndex(), "array");
        org.llvm.Value array = l.getBuilder().buildLoad(array_addr, "array");
        org.llvm.Value array_cast = l.getBuilder().buildBitCast(array,
                                    array_class.getElementType().llvmTypeField().pointerType(), "array_cast");
        org.llvm.Value [] indices = {index.llvmGen(l)};
        org.llvm.Value elem = l.getBuilder().buildInBoundsGEP(array_cast, "elem",
                              indices);
        l.getBuilder().buildStore(r, elem);
        if (array_class.getElementType().isClass() != null) {
            l.gcAssign(elem);
        }
    }
}
