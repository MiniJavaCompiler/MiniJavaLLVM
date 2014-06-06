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

/** Provides a representation for integer literals.
 */
public final class ArrayLiteral extends Literal {
    private Type type;
    private Expression [] literals;
    private Expression initialization;

    public ArrayLiteral(Position pos, Type type, Expression [] literals) {
        super(pos);
        this.type = type;
        this.literals = literals;
        this.initialization = new ConstructorInvocation(
            new Name(new Id(pos, type.toString())),
            new Args(new IntLiteral(pos, literals.length), null));

        for (int x = 0; x < literals.length; x++) {
            this.initialization = new ObjectInvocation(this.initialization, new Id(pos,
                    "initElem"),
                    new Args(new IntLiteral(pos, x), new Args(literals[x], null)));
        }
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        ArrayType a_type = type.check(ctxt).isArray();
        if (a_type == null) {
            throw new Failure(pos, "ArrayLiteral Type must be array type not " + type);
        } else {
            Type element = a_type.getElementType();
            for (Expression e : literals) {
                Type expr_type = e.typeOf(ctxt, env);
                if (!element.isSuperOf(expr_type)) {
                    throw new Failure(pos,
                    "One or more elements of array literal does not match array type");
                }
            }
        }
        return this.initialization.typeOf(ctxt, env);
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        this.initialization.compileExpr(a, free);
    }

    void compileExprOp(Assembly a, String op, int free) {
        this.initialization.compileExprOp(a, op, free);
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is false.
     */
    void branchFalse(Assembly a, String lab, int free) {
        /* no false */
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is true.
     */
    void branchTrue(Assembly a, String lab, int free) {
        /* always true */
        a.emit("jmp", lab);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return this.initialization.eval(st);
    }
    public org.llvm.Value llvmGen(LLVM l) {
        return this.initialization.llvmGen(l);
    }
}
