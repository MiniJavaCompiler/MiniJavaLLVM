// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;

/** Provides a representation for expressions that can appear
 *  in a statement.
 */
public abstract class StatementExpr extends Expression {
    public StatementExpr(Position pos) {
        super(pos);
    }

    /** Type check this expression in places where it is used as a statement.
     *  We override this method in Invocation to deal with methods that
     *  return void.
     */
    void checkExpr(Context ctxt, VarEnv env) throws Diagnostic {
        typeOf(ctxt, env);
    }
}
