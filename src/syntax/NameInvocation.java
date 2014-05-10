// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Represents an access to a method by an as yet unresolved name.
 */
public final class NameInvocation extends Invocation {
    private Name       name;
    private Invocation resolved;

    public NameInvocation(Name name, Args args) {
        super(name.getPos(), args);
        this.name     = name;
        this.resolved = null;
    }

    /** Calculate the type of this method invocation.
     */
    Type typeInvocation(Context ctxt, VarEnv env)
    throws Diagnostic {
        resolved = name.asMethod(ctxt, env, args);
        if (resolved == null) {
            throw new Failure(pos, "Undefined name " + name);
        }
        return resolved.typeInvocation(ctxt, env);
    }

    /** Generate code for this method invocation, leaving
     *  the result in the specified free variable.
     */
    void compileInvocation(Assembly a, int free) {
        resolved.compileInvocation(a, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return resolved.eval(st);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return resolved.llvmGen(l);
    }
}
