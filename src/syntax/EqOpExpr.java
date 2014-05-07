// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;

/** Provides a representation for equality and inequality tests.
 */
public abstract class EqOpExpr extends BinaryOp {
    public EqOpExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        try {
            Type lt = left.typeOf(ctxt, env);
            Type rt = right.typeOf(ctxt, env);
            if (!lt.equal(rt)) {
                ctxt.report(new Failure(pos,
                "Operands should have the same type, but the " +
                " left operand has type " + lt +
                " and the right operand has type " + rt));
            }
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        return Type.BOOLEAN;
    }
}
