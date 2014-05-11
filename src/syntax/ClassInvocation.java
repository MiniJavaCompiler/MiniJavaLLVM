// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Represents a class method invocation.
 */
public final class ClassInvocation extends Invocation {
    private ClassType cls;
    private String    name;
    private MethEnv   menv;

    public ClassInvocation(ClassType cls, Id id, Args args) {
        super(id.getPos(), args);
        this.cls  = cls;
        this.name = id.getName();
    }

    /** Calculate the type of this method invocation.
     */
    Type typeInvocation(Context ctxt, VarEnv env)
    throws Diagnostic {
        this.menv = cls.findMethod(name);
        if (this.menv == null) {
            throw new Failure(pos,
            "Cannot find method " + name + " in class " + cls);
        } else if (!this.menv.isStatic()) {
            throw new Failure(pos,
            "Cannot access method " + name +
            " without an object of class " + cls);
        }
        return checkInvocation(ctxt, env, this.menv);
    }

    /** Generate code for this method invocation, leaving
     *  the result in the specified free variable.
     */
    void compileInvocation(Assembly a, int free) {
        menv.compileInvocation(a, args, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return menv.call(st, null, args);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return llvmInvoke(l, menv, menv.getFunctionVal(), null);
    }
}
