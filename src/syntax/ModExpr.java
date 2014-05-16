// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for multiplication expressions.
 */
public final class ModExpr extends NumericOpExpr {
    public ModExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Return a true value to indicate that multiplication is commutative.
     */
    boolean commutes() {
        return false;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        compileOp(a, "imod", free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return new IntValue(left.eval(st).getInt() % right.eval(st).getInt());
    }

    public org.llvm.Value llvmBuildOp(LLVM l, org.llvm.Value left,
                                      org.llvm.Value right) {
        return l.getBuilder().buildSRem(left, right, "modtemp");
    }
}
