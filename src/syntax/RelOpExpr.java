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

import org.llvm.binding.LLVMLibrary.LLVMIntPredicate;
import org.llvm.binding.LLVMLibrary.LLVMRealPredicate;

/** Provides a representation for relational comparison operators.
 */
public abstract class RelOpExpr extends BinaryOp {
    abstract public LLVMIntPredicate getllvmRelOp();
    public RelOpExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        try {
            Type lt = left.typeOf(ctxt, env);
            Type rt = right.typeOf(ctxt, env);
            Type cast = null;
            if (!lt.isSuperOf(rt) && !rt.isSuperOf(lt)) {
                ctxt.report(new Failure(pos,
                "Operands should have the same type, but the " +
                " left operand has type " + lt +
                " and the right operand has type " + rt));
            } else if ((cast = Type.mixedClass(lt, rt)) != null) {
                if (rt.equal(cast)) {
                    left = new CastExpr(pos, cast, left);
                } else {
                    right = new CastExpr(pos, cast, right);
                }
            }
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        return Type.BOOLEAN;
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return l.getBuilder().buildICmp(getllvmRelOp(), left.llvmGen(l),
                                        right.llvmGen(l), "cmptmp");
    }
    public abstract Value.COMPARE_OP getInterpRelOp();

    public Value eval(State st) {
        return BoolValue.make(left.eval(st).compare(getInterpRelOp(), right.eval(st)));
    }
}
