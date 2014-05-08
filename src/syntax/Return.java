// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for return statements.
 */
public final class Return extends Statement {
    private Expression result;

    public Return(Position pos, Expression result) {
        super(pos);
        this.result = result;
    }

    public Return(Position pos) {
        this(pos, null);
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        Type rt = ctxt.getCurrMethod().getType();
        if (result != null) {
            if (rt != null) {
                try {
                    Type it = result.typeOf(ctxt, env);
                    if (!rt.isSuperOf(it)) {
                        ctxt.report(new Failure(pos,
                                                "Cannot return a value of type " + it +
                                                " where a value of type " + rt +
                                                " is required"));
                    }
                } catch (Diagnostic d) {
                    ctxt.report(d);
                }
            } else {
                ctxt.report(new Failure(pos,
                                        "Method should not return value"));
            }
        } else if (rt != null) {
            ctxt.report(new Failure(pos, "A return value is required"));
        }
        return false;
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        if (result != null) {
            result.compileExpr(a);
        }
        a.emitEpilogue();
    }

    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        compile(a);
    }

    /** Emit code that executes this statement and then returns from the
     *  current method.
     */
    public void compileRet(Assembly a) {
        compile(a);
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        return (result == null) ? Value.NULL : result.eval(st);
    }

    public void llvmGen(LLVM l) {
        l.getBuilder().buildRet(result.llvmGen(l));
    }
}
