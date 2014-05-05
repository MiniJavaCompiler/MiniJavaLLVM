// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for addition expressions.
 */
public final class DivExpr extends NumericOpExpr {
    public DivExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Return a false value to indicate that division is not commutative.
     */
    boolean commutes() {
        return false;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        // TODO: The following code is not correct!
        // If only somebody would fix it for me ... :-)
        compileOp(a, "divl", free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return new IntValue(left.eval(st).getInt() / right.eval(st).getInt());
    }
    public Object accept(ExprVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
