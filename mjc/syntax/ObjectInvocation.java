// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Represents an instance method invocation.
 */
public final class ObjectInvocation extends Invocation {
    private Expression object;
    private String     name;
    private MethEnv    menv;

    public ObjectInvocation(Expression object, Id id, Args args) {
        super(id.getPos(), args);
        this.object = object;
        this.name   = id.getName();
    }

    /** Calculate the type of this method invocation.
     */
    Type typeInvocation(Context ctxt, VarEnv env)
    throws Diagnostic {
        Type receiver = object.typeOf(ctxt, env);
        ClassType cls = receiver.isClass();
        if (cls == null) {
            throw new Failure(pos,
            "Cannot access field " + name +
            " in a value of type " + receiver);
        } else if ((this.menv = cls.findMethod(name)) == null) {
            throw new Failure(pos,
            "Cannot find method " + name + " in class " + cls);
        }
        return checkInvocation(ctxt, env, this.menv);
    }

    /** Generate code for this method invocation, leaving
     *  the result in the specified free variable.
     */
    void compileInvocation(Assembly a, int free) {
        object.compileExpr(a, 0);
        menv.compileInvocation(a, args, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return menv.call(st, object.eval(st).getObj(), args);
    }
}
