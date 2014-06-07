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

/** Provides a representation for binary expressions with numeric
 *  operands.
 */
public abstract class NumericOpExpr extends BinaryOp {
    public NumericOpExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        try {
            required(ctxt, "Left operand",  left.typeOf(ctxt, env),  Type.INT);
            required(ctxt, "Right operand", right.typeOf(ctxt, env), Type.INT);
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        return Type.INT;
    }
    public org.llvm.Value llvmGen(LLVM l) {
        org.llvm.Value llvm_left = left.llvmGen(l);
        org.llvm.Value llvm_right = right.llvmGen(l);

        return llvmBuildOp(l, llvm_left, llvm_right);
    }

    public abstract org.llvm.Value llvmBuildOp(LLVM l, org.llvm.Value left,
            org.llvm.Value right);
}
