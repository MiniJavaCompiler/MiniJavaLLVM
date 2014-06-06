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

/** Provides a representation for bitwise binary connectives.
 */
public abstract class BitOpExpr extends BinaryOp {
    public BitOpExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        Type lt = left.typeOf(ctxt, env);
        Type rt = right.typeOf(ctxt, env);
        if (lt.equal(Type.BOOLEAN) && rt.equal(Type.BOOLEAN)) {
            return Type.BOOLEAN;
        } else if (lt.equal(Type.INT) && rt.equal(Type.INT)) {
            return Type.INT;
        } else {
            ctxt.report(new Failure(pos, "Incompatible operand types"));
            return Type.BOOLEAN; // a guess, trying to minimize further errors
        }
    }

    // Bitwise operators set the flags automatically, so we don't need to
    // force a cmpl before the conditional jump ...

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is false.
     */
    void branchFalse(Assembly a, String lab, int free) {
        compileExpr(a, free);
        a.emit("jz", lab);
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is true.
     */
    void branchTrue(Assembly a, String lab, int free) {
        compileExpr(a, free);
        a.emit("jnz", lab);
    }
    public abstract org.llvm.Value llvmGenBitOp(LLVM l, org.llvm.Value l_v,
            org.llvm.Value r_v);
    public org.llvm.Value llvmGen(LLVM l) {
        org.llvm.Value l_v = left.llvmGen(l);
        org.llvm.Value r_v = right.llvmGen(l);
        return llvmGenBitOp(l, l_v, r_v);
    }
}
