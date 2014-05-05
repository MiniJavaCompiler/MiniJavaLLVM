// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for if-then-else (and if-then) statements.
 */
public final class IfThenElse extends Statement {
    private Expression test;
    private Statement ifTrue;
    private Statement ifFalse;
    public IfThenElse(Position pos,
                      Expression test, Statement ifTrue, Statement ifFalse) {
        super(pos);
        this.test    = test;
        this.ifTrue  = ifTrue;
        this.ifFalse = ifFalse;
    }
    public IfThenElse(Position pos, Expression test, Statement ifTrue) {
        this(pos, test, ifTrue, null);
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
        return (ifTrue.check(ctxt, env, frameOffset) &
                (ifFalse == null || ifFalse.check(ctxt, env, frameOffset)));
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        String l1 = a.newLabel();
        test.branchFalse(a, l1);
        if (ifFalse != null) {
            String l2 = a.newLabel();
            ifTrue.compileThen(a, l2);
            a.emitLabel(l1);
            ifFalse.compile(a);
            a.emitLabel(l2);
        } else {
            ifTrue.compile(a);
            a.emitLabel(l1);
        }
    }

    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        if (ifFalse != null) {
            String l1 = a.newLabel();
            test.branchFalse(a, l1);
            ifTrue.compileThen(a, lab);
            a.emitLabel(l1);
            ifFalse.compileThen(a, lab);
        } else {
            test.branchFalse(a, lab);
            ifTrue.compileThen(a, lab);
        }
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        if (test.eval(st).getBool()) {
            return (ifTrue == null)  ? null : ifTrue.exec(st);
        } else {
            return (ifFalse == null) ? null : ifFalse.exec(st);
        }
    }
    public Object accept(StmtVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
