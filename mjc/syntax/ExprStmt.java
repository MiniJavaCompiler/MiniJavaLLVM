// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for expressions used as statements.
 */
public final class ExprStmt extends Statement {
    private StatementExpr expr;

    public ExprStmt(Position pos, StatementExpr expr) {
        super(pos);
        this.expr = expr;
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        try {
            expr.checkExpr(ctxt, env);
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        return true;
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        expr.compileExpr(a);
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        expr.eval(st);
        return null;
    }
    public Object accept(StmtVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
