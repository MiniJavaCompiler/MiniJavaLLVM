/*
 * MiniJava Compiler - X86, LLVM Compiler/Interpreter for MiniJava.
 * Copyright (C) 2014, 2008 Mitch Souders, Mark A. Smith, Mark P. Jones
 *
 * MiniJava Compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * MiniJava Compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MiniJava Compiler; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


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
    public Statement [] getStmts() {
        return stmts;
    }
    public void appendStatement(Statement new_s) {
        Statement[] new_stmts = new Statement[stmts.length + 1];
        for (int x = 0; x < stmts.length; x++) {
            new_stmts[x] = stmts[x];
        }
        new_stmts[stmts.length] = new_s;
        stmts = new_stmts;
    }
    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        Iterator<Statement> iter = Arrays.asList(stmts).iterator();
        boolean good = true;
        if (iter.hasNext()) {
            Statement s = iter.next();
            good = s.check(ctxt, env, frameOffset, iter);
        }
        assert(!iter.hasNext());
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
            a.emit("// " + s.getPos().describe());
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
        l.enterScope();
        for (Statement ss : stmts) {
            ss.llvmGen(l);
        }
        l.leaveScope();
    }
}
