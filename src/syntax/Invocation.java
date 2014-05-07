// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;

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
        a.spillAll(free);
        compileInvocation(a, free);
        a.unspillAll(free);
    }
}
