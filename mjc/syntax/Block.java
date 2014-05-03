// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for blocks.
 */
public final class Block extends Statement {
    private Stmts stmts;
    public Block(Position pos, Stmts stmts) {
        super(pos);
        this.stmts = stmts;
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        return (stmts==null) || stmts.check(ctxt, env, frameOffset);
    }

    /** Emit code to execute this statement.
     */ 
    void compile(Assembly a) {
        if (stmts!=null) {
            stmts.compile(a);
        }
    }
    
    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        if (stmts!=null) {
            stmts.compileThen(a, lab);
        } else {
            a.emit("jmp", lab);
        }
    }

    /** Emit code that executes this statement and then returns from
     *  the current method.
     */
    public void compileRet(Assembly a) {
        if (stmts!=null) {
            stmts.compileRet(a);
        } else {
            a.emitEpilogue();
        }
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        Value v = null;
        for (Stmts ss=stmts; v==null && ss!=null; ss=ss.next) {
            v = ss.exec(st);
        }
        return v;
    }
}
