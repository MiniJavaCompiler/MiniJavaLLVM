// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for the "this" pointer to the current object.
 */
public final class This extends Expression {
    private int size;

    public This(Position pos) {
        super(pos);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        if (ctxt.isStatic()) {
            throw new Failure(pos, "Cannot access this in a static context");
        }
        size = ctxt.getCurrMethod().getSize();
        return ctxt.getCurrClass();
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        a.loadThis(size, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return st.getThis(size);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return l.getNamedValue("this");
    }
}
