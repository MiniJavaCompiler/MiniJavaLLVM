// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Represents a method invocation through this.
 */
public final class ThisInvocation extends Invocation {
    private String  name;
    private MethEnv menv;
    private int     size;

    public ThisInvocation(Id id, Args args, MethEnv menv) {
        super(id.getPos(), args);
        this.name = id.getName();
        this.menv = menv;
    }

    /** Calculate the type of this method invocation.
     */
    Type typeInvocation(Context ctxt, VarEnv env) throws Diagnostic {
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
