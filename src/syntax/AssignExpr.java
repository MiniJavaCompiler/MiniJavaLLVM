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

/** Provides a representation for assignment expressions.
 */
public final class AssignExpr extends StatementExpr {
    private LeftHandSide lhs;
    private Expression rhs;

    public Expression getRHS() {
        return rhs;
    }
    public AssignExpr(Position pos, LeftHandSide lhs, Expression rhs) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        Type lt = lhs.typeOf(ctxt, env);
        Type rt = rhs.typeOf(ctxt, env);

        if (!lt.isSuperOf(rt)) {
            throw new Failure(pos, "Cannot assign value of type " + rt +
            " to variable of type " + lt);
        } else if (!lt.equal(rt)) {
            rhs = new CastExpr(pos, lt, rhs);
        }

        return lt;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        rhs.compileExpr(a, free);
        lhs.saveVar(a, free);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        org.llvm.Value right = rhs.llvmGen(l);
        lhs.llvmSave(l, right);
        return right;
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        Value val = rhs.eval(st);
        lhs.save(st, val);
        return val;
    }
}
