// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;

/** Provides a representation for expressions using unary operators.
 */
public abstract class UnaryOp extends Expression {
    protected Expression expr;
    public UnaryOp(Position pos, Expression expr) {
        super(pos);
        this.expr = expr;
    }
}
