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

/** Provides a representation for unary negation.
 */
public final class NegExpr extends UnaryOp {
    public NegExpr(Position pos, Expression expr) {
        super(pos, expr);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        try {
            required(ctxt, "operand", expr.typeOf(ctxt, env), Type.INT);
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        return Type.INT;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        expr.compileExpr(a, free);
        a.emit("negl", a.reg(free));
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return new IntValue(-expr.eval(st).getInt());
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return l.getBuilder().buildNeg(expr.llvmGen(l), "neg_temp");
    }

}
