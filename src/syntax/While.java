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

import org.llvm.Builder;
import org.llvm.BasicBlock;

/** Provides a representation for while statements.
 */
public final class While extends Statement {
    private Expression test;
    private Statement body;

    public While(Position pos, Expression test, Statement body) {
        super(pos);
        this.test = test;
        this.body = body;
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        try {
            if (!test.typeOf(ctxt, env).equal(Type.BOOLEAN)) {
                ctxt.report(new Failure(pos,
                                        "Boolean valued expression required for test"));
            }
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        body.check(ctxt, env, frameOffset);
        return true;
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        String l1 = a.newLabel();
        String l2 = a.newLabel();
        a.emitLabel(l1);
        test.branchFalse(a, l2);
        body.compileThen(a, l1);
        a.emitLabel(l2);
    }

    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        String l1 = a.newLabel();
        a.emitLabel(l1);
        test.branchFalse(a, lab);
        body.compileThen(a, l1);
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        Value v = null;
        while (v == null && test.eval(st).getBool()) {
            v = body.exec(st);
        }
        return v;
    }

    public void llvmGen(LLVM l) {
        Builder b = l.getBuilder();
        BasicBlock loopCond = l.getFunction().appendBasicBlock("while_cond");
        BasicBlock loopBody = l.getFunction().appendBasicBlock("while_body");
        BasicBlock loopEnd = l.getFunction().appendBasicBlock("while_end");
        b.buildBr(loopCond);

        b.positionBuilderAtEnd(loopCond);
        b.buildCondBr(test.llvmGen(l), loopBody, loopEnd);

        b.positionBuilderAtEnd(loopBody);
        body.llvmGen(l);
        b.buildBr(loopCond);

        b.positionBuilderAtEnd(loopEnd);
    }
}
