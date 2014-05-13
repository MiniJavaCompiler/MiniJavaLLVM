// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;
import java.util.Iterator;
import java.util.Arrays;

/** Provides a representation for blocks.
 */
public final class Block extends Statement {
    private Statement [] stmts;
    public Block(Position pos, Statement [] stmts) {
        super(pos);
        this.stmts = stmts;
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        Iterator<Statement> iter = Arrays.asList(stmts).iterator();
        boolean good = true;
        while (iter.hasNext()) {
            Statement s = iter.next();
            good = good && s.check(ctxt, env, frameOffset, iter);
        }
        return good;
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        for (Statement s : stmts) {
            s.compile(a);
        }
    }

    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        for (Statement s : stmts) {
            s.compile(a);
        }
        a.emit("jmp", lab);
    }

    /** Emit code that executes this statement and then returns from
     *  the current method.
     */
    public void compileRet(Assembly a) {
        for (Statement s : stmts) {
            s.compile(a);
        }
        a.emitEpilogue();
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        Value v = null;
        for (Statement ss : stmts) {
            v = ss.exec(st);
            if (v != null) {
                return v;
            }
        }
        return v;
    }

    public void llvmGen(LLVM l) {
        for (Statement ss : stmts) {
            ss.llvmGen(l);
        }
    }
}
