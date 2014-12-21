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

import java.util.ArrayList;
import org.llvm.TypeRef;

/** Provides a representation for method invocations.
 */
public abstract class Invocation extends StatementExpr {
    protected Args args;

    public Invocation(Position pos, Args args) {
        super(pos);
        this.args = args;
    }

    /** Type check this expression in places where it is used as a statement.
     *  We override this method in Invocation to deal with methods that
     *  return void.
     */
    void checkExpr(Context ctxt, VarEnv env)
    throws Diagnostic {
        typeInvocation(ctxt, env);
    }

    /** Calculate the type of this method invocation.
     */
    abstract Type typeInvocation(Context ctxt, VarEnv env)
    throws Diagnostic;

    /** Check arguments of a method invocation.
     */
    Type checkInvocation(Context ctxt, VarEnv env, MethEnv menv) {
        menv.accessCheck(ctxt, pos);
        menv.checkArgs(pos, ctxt, env, args);
        return menv.getType();
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        Type result = typeInvocation(ctxt, env);
        if (result == null) {
            throw new Failure(pos, "Method does not return a value");
        }
        return result;
    }

    /** Generate code for this method invocation, leaving
     *  the result in the specified free variable.
     */
    abstract void compileInvocation(Assembly a, int free);

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        compileInvocation(a, free);
    }

    public org.llvm.Value llvmInvoke(LLVM l, MethEnv menv, org.llvm.Value function,
                                     org.llvm.Value this_ptr) {
        /* static methods will provide null for this_ptr */
        ArrayList<org.llvm.Value> func_args = new ArrayList<org.llvm.Value>();

        int i = 0;
        if (this_ptr != null) {
            func_args.add(l.getBuilder().buildBitCast(this_ptr,
                          menv.getOwner().llvmTypeField(), "cast"));
            i++;
        }

        if (args != null) {
            for (Args a : args) {
                org.llvm.Value arg_val = a.getArg().llvmGen(l);
                func_args.add(arg_val);
                i++;
            }
        }

        String func_name = menv.getName();
        String name = "call_" + func_name;
        if (menv.getType() == Type.VOID) {
            name = "";
        }
        return l.getBuilder().buildCall(function, name, func_args);
    }
}
