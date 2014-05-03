// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;

/** Provides a representation for bitwise binary connectives.
 */
public abstract class BitOpExpr extends BinaryOp {
    public BitOpExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        Type lt = left.typeOf(ctxt, env);
        Type rt = right.typeOf(ctxt, env);
        if (lt.equal(Type.BOOLEAN) && rt.equal(Type.BOOLEAN)) {
            return Type.BOOLEAN;
        } else if (lt.equal(Type.INT) && rt.equal(Type.INT)) {
            return Type.INT;
        } else {
            ctxt.report(new Failure(pos, "Incompatible operand types"));
            return Type.BOOLEAN; // a guess, trying to minimize further errors
        }
    }

    // Bitwise operators set the flags automatically, so we don't need to
    // force a cmpl before the conditional jump ...

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is false.
     */
    void branchFalse(Assembly a, String lab, int free) {
        compileExpr(a, free);
        a.emit("jz", lab);
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is true.
     */
    void branchTrue(Assembly a, String lab, int free) {
        compileExpr(a, free);
        a.emit("jnz", lab);
    }
}
