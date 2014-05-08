// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM
//
// Modified Mark Smith, Portland State University
// Added compileLLVMExpr
// Apr 27, 2014

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for addition expressions.
 */
public final class AddExpr extends NumericOpExpr {
    public AddExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Return a true value to indicate that addition is commutative.
     */
    boolean commutes() {
        return true;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        compileOp(a, "addl", free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return new IntValue(left.eval(st).getInt() + right.eval(st).getInt());
    }

    public org.llvm.Value llvmBuildOp(LLVM l, org.llvm.Value left,
                                      org.llvm.Value right) {
        return l.getBuilder().buildAdd(left, right, "addtemp");
    }
}
