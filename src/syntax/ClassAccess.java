// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Represents an access to a class variable.
 */
public final class ClassAccess extends FieldAccess {
    private FieldEnv env;

    public ClassAccess(FieldEnv env) {
        super(env.getPos());
        this.env  = env;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        if (!this.env.isStatic()) {
            throw new Failure(pos,
            "Cannot access field " + this.env.getName() +
            " without an object of class " + this.env.getOwner());
        }
        this.env.accessCheck(ctxt, pos);
        return this.env.getType();
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        env.loadField(a, free);
    }

    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    void saveVar(Assembly a, int free) {
        env.saveField(a, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return env.getField(null);  // static analysis ensures object not used
    }

    /** Save a value in the location specified by this left hand side.
     */
    public void save(State st, Value val) {
        env.setField(null, val);  // static analysis ensures object not used
    }
}
