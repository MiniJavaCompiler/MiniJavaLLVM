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

/** Provides a representation for bitwise or expressions (|).
 */
public final class BitOrExpr extends BitOpExpr {
    public BitOrExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Return a true value to indicate that bitwise or is commutative.
     */
    boolean commutes() {
        return true;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        compileOp(a, "orl", free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return new IntValue(left.eval(st).getInt() | right.eval(st).getInt());
    }

    public org.llvm.Value llvmGenBitOp(LLVM l, org.llvm.Value l_v,
                                       org.llvm.Value r_v) {
        return l.getBuilder().buildOr(l_v, r_v, "ortemp");
    }

}
