// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for local variable declarations in a block.
 */
public class LocalVarDecl extends Stmts {
    private Position pos;
    private Type type;
    private VarDecls varDecls;
    public LocalVarDecl(Position pos, Type type,
                        VarDecls varDecls, Stmts next) {
        super(next);
        this.pos      = pos;
        this.type     = type;
        this.varDecls = varDecls;
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        type = type.check(ctxt);
        if (type != null) {
            int size = type.size();
            for (VarDecls vs = varDecls; vs != null; vs = vs.getNext()) {
                if (VarEnv.find(vs.getId().getName(), env) != null) {
                    ctxt.report(new Failure(pos,
                                            "Repeated definition for variable " + vs.getId()));
                } else {
                    frameOffset -= size;
                    env = new VarEnv(vs.getId(), type, frameOffset, env);
                }
            }
            ctxt.reserveSpace(frameOffset);
        }
        if (next == null) {
            ctxt.report(new Failure(pos, "Declarations have no use"));
            return true;
        } else {
            return next.check(ctxt, env, frameOffset);
        }
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        next.compile(a);            // static analysis ensures next!=null
    }

    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        next.compileThen(a, lab);   // static analysis ensures next!=null
    }

    /** Emit code that executes this statement and then returns from the
     *  current method.
     */
    public void compileRet(Assembly a) {
        next.compileRet(a);         // static analysis ensures next!=null
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        return null;
    }
}
