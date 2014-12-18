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

import java.util.ArrayList;
/** Provides a representation for integer literals.
 */
public final class ArrayLiteral extends StatementExpr {
    private Type type;
    private Expression [] literals;
    private TmpAccess tmp;
    private Expression object;
    private Expression [] inits;

    private void buildInits(TmpAccess tmp) {

    }

    private void commonCons(Type type, Expression [] literals) {
        this.type = type;
        this.literals = literals;
        this.tmp = new TmpAccess(pos, type);
        this.object = new ConstructorInvocation(
            new Name(new Id(pos, type.toString())),
            new Args(new IntLiteral(pos, literals.length), null));
    }
    public ArrayLiteral(Position pos, Type type, Args args) {
        super(pos);
        ArrayList<Expression> list = new ArrayList<Expression>();
        if (args != null) {
            for (Args a : args) {
                list.add(a.getArg());
            }
        }
        commonCons(type, list.toArray(new Expression[0]));
    }

    public ArrayLiteral(Position pos, Type type, Expression [] literals) {
        super(pos);
        commonCons(type, literals);
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
            inits = new Expression[literals.length];
            for (int x = 0; x < literals.length; x++) {
                inits[x] = new AssignExpr(pos, new ArrayAccess(pos, tmp, new IntLiteral(pos, x),
                                          false), literals[x]);
                inits[x].typeOf(ctxt, env);
            }
        }
        return this.object.typeOf(ctxt, env);
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        this.object.compileExpr(a, free);
        tmp.setTmp(free);
        a.spill(free + 1);
        for (Expression e : inits) {
            e.compileExpr(a, free + 1);
        }
        a.unspill(free + 1);
    }

    void compileExprOp(Assembly a, String op, int free) {
        this.object.compileExprOp(a, op, free);
        tmp.setTmp(free);
        a.spill(free + 1);
        for (Expression e : inits) {
            e.compileExpr(a, free + 1);
        }
        a.unspill(free + 1);
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
        Value v = this.object.eval(st);
        tmp.setTmp(v);
        for (Expression e : inits) {
            e.eval(st);
        }
        return v;
    }
    public org.llvm.Value llvmGen(LLVM l) {
        org.llvm.Value v = this.object.llvmGen(l);
        tmp.setTmp(v);
        for (Expression e : inits) {
            e.llvmGen(l);
        }
        return v;
    }
}
