// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import checker.*;
import compiler.*;
import codegen.*;
import interp.*;

/** Provides a representation for while statements.
 */
public final class DoWhile extends Statement {
    private Expression test;
    private Statement body;
    public DoWhile(Position pos, Expression test, Statement body) {
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
        return body.check(ctxt, env, frameOffset);
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        String l1 = a.newLabel();
        a.emitLabel(l1);
        body.compile(a);
        test.branchTrue(a, l1);
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        Value v = null;
        do {
            v = body.exec(st);
        } while (v == null && test.eval(st).getBool());
        return v;
    }
}
