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
import codegen.*;
import interp.*;
import org.llvm.BasicBlock;
import org.llvm.Builder;

/** Provides a representation for conditional or expressions (||).
 */
public final class CondOrExpr extends LogicOpExpr {
    public CondOrExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        String l1 = a.newLabel();
        String l2 = a.newLabel();
        left.branchTrue(a, l1, free);
        right.branchTrue(a, l1, free);
        a.emit("movl", a.immed(0), a.reg(free));
        a.emit("jmp", l2);

        a.emitLabel(l1);
        a.emit("movl", a.immed(1), a.reg(free));

        a.emitLabel(l2);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        Builder b = l.getBuilder();
        BasicBlock fBranch = l.getFunction().appendBasicBlock("cond_false");
        BasicBlock tBranch = l.getFunction().appendBasicBlock("cond_true");
        org.llvm.Value cmptmp = b.buildAlloca(Type.BOOLEAN.llvmType(), "cmptmp");
        org.llvm.Value res = left.llvmGen(l);

        b.buildStore(res, cmptmp);
        b.buildCondBr(res, tBranch, fBranch);

        b.positionBuilderAtEnd(fBranch);
        b.buildStore(right.llvmGen(l), cmptmp);
        b.buildBr(tBranch);

        b.positionBuilderAtEnd(tBranch);
        return b.buildLoad(cmptmp, "cmpres");
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is false.
     */
    void branchFalse(Assembly a, String lab, int free) {
        String l1 = a.newLabel();
        left.branchTrue(a, l1, free);
        right.branchFalse(a, lab, free);
        a.emitLabel(l1);
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is true.
     */
    void branchTrue(Assembly a, String lab, int free) {
        left.branchTrue(a, lab, free);
        right.branchTrue(a, lab, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return BoolValue.make(left.eval(st).getBool()
                              || right.eval(st).getBool());
    }
}
