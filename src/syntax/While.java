// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for while statements.
 */
public final class While extends Statement {
    private Expression test;
    private Statement body;

    public While(Position pos, Expression test, Statement body) {
        super(pos);
        this.test = test;
        this.body = body;
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        try {
            if (!test.typeOf(ctxt, env).equal(Type.BOOLEAN)) {
                ctxt.report(new Failure(pos,
                                        "Boolean valued expression required for test"));
            }
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        body.check(ctxt, env, frameOffset);
        return true;
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        String l1 = a.newLabel();
        String l2 = a.newLabel();
        a.emitLabel(l1);
        test.branchFalse(a, l2);
        body.compileThen(a, l1);
        a.emitLabel(l2);
    }

    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        String l1 = a.newLabel();
        a.emitLabel(l1);
        test.branchFalse(a, lab);
        body.compileThen(a, l1);
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        Value v = null;
        while (v == null && test.eval(st).getBool()) {
            v = body.exec(st);
        }
        return v;
    }
}
