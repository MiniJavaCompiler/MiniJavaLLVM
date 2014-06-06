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
import org.llvm.BasicBlock;
import org.llvm.Builder;

/** Provides a representation for if-then-else (and if-then) statements.
 */
public final class IfThenElse extends Statement {
    private Expression test;
    private Statement ifTrue;
    private Statement ifFalse;
    public IfThenElse(Position pos,
                      Expression test, Statement ifTrue, Statement ifFalse) {
        super(pos);
        this.test    = test;
        this.ifTrue  = ifTrue;
        this.ifFalse = ifFalse;
    }
    public IfThenElse(Position pos, Expression test, Statement ifTrue) {
        this(pos, test, ifTrue, null);
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
        boolean t_check = ifTrue.check(ctxt, env, frameOffset);
        boolean f_check = (ifFalse == null || ifFalse.check(ctxt, env, frameOffset));
        return t_check && f_check;
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        String l1 = a.newLabel();
        test.branchFalse(a, l1);
        if (ifFalse != null) {
            String l2 = a.newLabel();
            ifTrue.compileThen(a, l2);
            a.emitLabel(l1);
            ifFalse.compile(a);
            a.emitLabel(l2);
        } else {
            ifTrue.compile(a);
            a.emitLabel(l1);
        }
    }

    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        if (ifFalse != null) {
            String l1 = a.newLabel();
            test.branchFalse(a, l1);
            ifTrue.compileThen(a, lab);
            a.emitLabel(l1);
            ifFalse.compileThen(a, lab);
        } else {
            test.branchFalse(a, lab);
            ifTrue.compileThen(a, lab);
        }
    }

    public void llvmGen(LLVM l) {
        Builder b = l.getBuilder();
        BasicBlock fBranch = l.getFunction().appendBasicBlock("if_false");
        BasicBlock tBranch = l.getFunction().appendBasicBlock("if_true");
        BasicBlock endBranch = l.getFunction().appendBasicBlock("if_end");
        org.llvm.Value res = test.llvmGen(l);
        b.buildCondBr(res, tBranch, fBranch);

        b.positionBuilderAtEnd(tBranch);
        ifTrue.llvmGen(l);
        b.buildBr(endBranch);

        b.positionBuilderAtEnd(fBranch);
        ifFalse.llvmGen(l);
        b.buildBr(endBranch);

        b.positionBuilderAtEnd(endBranch);
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        if (test.eval(st).getBool()) {
            return (ifTrue == null)  ? null : ifTrue.exec(st);
        } else {
            return (ifFalse == null) ? null : ifFalse.exec(st);
        }
    }
}
