// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for a statement in a block.
 */
public class BlockStatement extends Stmts {
    private Statement stmt;
    public BlockStatement(Statement stmt, Stmts next) {
        super(next);
        this.stmt = stmt;
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        return stmt.check(ctxt, env, frameOffset) &
               (next == null || next.check(ctxt, env, frameOffset));
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        stmt.compile(a);
        if (next != null) {
            next.compile(a);
        }
    }

    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        if (next != null) {
            stmt.compile(a);
            next.compileThen(a, lab);
        } else {
            stmt.compileThen(a, lab);
        }
    }

    /** Emit code that executes this statement and then returns from the
     *  current method.
     */
    public void compileRet(Assembly a) {
        if (next != null) {
            stmt.compile(a);
            next.compileRet(a);
        } else {
            stmt.compileRet(a);
        }
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        return stmt.exec(st);
    }
}
