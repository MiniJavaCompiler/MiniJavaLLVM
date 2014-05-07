// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

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

    public SuperInvocation(Id id, Args args) {
        super(id.getPos(), args);
        this.name = id.getName();
    }

    /** Calculate the type of this method invocation.
     */
    Type typeInvocation(Context ctxt, VarEnv env) throws Diagnostic {
        ClassType sup = ctxt.getCurrClass().getSuper();
        if (sup == null) {
            throw new Failure(pos, "Current class has no super class");
        } else if (ctxt.isStatic()) {
            throw new Failure(pos,
            "Cannot access a super class in a static context");
        } else if ((this.menv = sup.findMethod(name)) == null) {
            throw new Failure(pos,
            "Cannot find method " + name + " in superclass");
        }
        size = ctxt.getCurrMethod().getSize();
        return checkInvocation(ctxt, env, this.menv);
    }

    /** Generate code for this method invocation, leaving
     *  the result in the specified free variable.
     */
    void compileInvocation(Assembly a, int free) {
        if (!menv.isStatic()) {
            a.loadThis(size, 0);
        }
        menv.compileInvocation(a, args, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return menv.call(st, st.getThis(size), args);
    }
}
