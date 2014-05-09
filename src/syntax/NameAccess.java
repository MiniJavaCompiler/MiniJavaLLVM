// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Represents an access to a field by an as yet unresolved name.
 */
public final class NameAccess extends FieldAccess {
    private Name        name;
    private FieldAccess resolved;

    public NameAccess(Name name) {
        super(name.getPos());
        this.name     = name;
        this.resolved = null;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        resolved = name.asValue(ctxt, env);
        if (resolved == null) {
            throw new Failure(pos, "Undefined name " + name);
        }
        return resolved.typeOf(ctxt, env);
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        resolved.compileExpr(a, free);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return resolved.llvmGen(l);
    }

    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    void saveVar(Assembly a, int free) {
        resolved.saveVar(a, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return resolved.eval(st);
    }

    /** Save a value in the location specified by this left hand side.
     */
    public void save(State st, Value val) {
        resolved.save(st, val);
    }
}
