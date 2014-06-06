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

/** Provides a representation for expressions.
 */
public class NullCheck extends Expression {
    private Expression nullCheck;
    private Expression object;
    private boolean typeResolved;
    public NullCheck(Position pos, Expression obj) {
        super(pos);
        this.typeResolved = false;
        this.object = obj;
        this.nullCheck =
            new NameInvocation(
            new Name(new Name(new Id(pos, "Object")), new Id(pos, "nullCheck")),
            new Args(new StringLiteral(pos, pos.getFilename()),
                     new Args(new IntLiteral(pos, pos.getRow()),
                              new Args(obj, null))));
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return nullCheck.llvmGen(l);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        if (!typeResolved) {
            Type f = nullCheck.typeOf(ctxt, env);
            Type t = object.typeOf(ctxt, env);
            if (!f.equals(ctxt.findClass("Object"))) {
                throw new Failure(pos, "Object.nullCheck must return type Object (returns " + f
                + ")");
            } else {
                /* this is a very safe upcast */
                nullCheck = new CastExpr(pos, t, nullCheck);
            }
            typeResolved = true;
        }
        return nullCheck.typeOf(ctxt, env);
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        nullCheck.compileExpr(a, free);
    }
    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return nullCheck.eval(st);
    }
}
