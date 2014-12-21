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

/** Represents a method invocation through the superclass.
 */
public final class SuperInvocation extends Invocation {
    private String  name;
    private MethEnv menv;
    private int     size;
    private boolean unnamed;
    private boolean isFirst;
    private Expression object;

    public SuperInvocation(Position pos, Id id, Args args) {
        super(pos, args);
        this.unnamed = (id == null);
        this.name = null;
        this.isFirst = false;
        if (!unnamed) {
            this.name = id.getName();
        }
        this.object = null;
    }

    public void setFirst() {
        this.isFirst = true;
    }
    /** Calculate the type of this method invocation.
     */
    Type typeInvocation(Context ctxt, VarEnv env) throws Diagnostic {
        ClassType sup = ctxt.getCurrClass().getSuper();
        if (sup != null && unnamed) {
            this.name = sup.getId().getName();
        }
        if (sup == null) {
            throw new Failure(pos, "Current class has no super class");
        } else if (name == null) {
            throw new Failure(pos, "No super constructor determined for super()");
        } else if (!isFirst && unnamed) {
            throw new Failure(pos,
            "Super constructor must be first instruction in constructor.");
        } else if (unnamed && ctxt.getCurrMethod() != null && !ctxt.getCurrMethod().isConstructor()) {
            throw new Failure(pos, "Super constructor can only be in a constructor.");
        } else if (ctxt.isStatic()) {
            throw new Failure(pos,
            "Cannot access a super class in a static context");
        } else if ((this.menv = sup.findMethodCall(name, ctxt, env, args)) == null) {
            throw new Failure(pos,
            "Cannot find corresponding superclass constructor.");
        }

        if (this.menv != null && !this.menv.isStatic()) {
            this.object = new This(pos);
            this.object.typeOf(ctxt, env);
        }
        size = ctxt.getCurrMethod().getSize();
        return checkInvocation(ctxt, env, this.menv);
    }

    /** Generate code for this method invocation, leaving
     *  the result in the specified free variable.
     */
    void compileInvocation(Assembly a, int free) {
        menv.compileInvocation(a, object, args, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return menv.call(st, st.getThis(size), args);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return llvmInvoke(l, menv, menv.getFunctionVal(l), l.getFunction().getParam(0));
    }
}
