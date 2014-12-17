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

/** Provides a representation for integer literals.
 */
public final class IntLiteral extends Literal {
    private int value;
    private String str_value;
    private boolean resolved;

    public int getValue() {
        return value;
    }
    public boolean setNegative() {
        if (resolved) {
            assert(false); /* cannot change sign of resolved integer */
        }
        if (str_value.charAt(0) >= '0' && str_value.charAt(0) <= '9') {
            str_value = '-' + str_value;
            return true;
        } else if (str_value.charAt(0) == '-') {
            return false;
        } else {
            // unhandled character
            assert(false);
            return false;
        }
    }

    public IntLiteral(Position pos, int value) {
        super(pos);
        this.value = value;
        resolved = true;
    }
    public IntLiteral(Position pos, String str) {
        super(pos);
        this.str_value = str;
        resolved = false;
    }
    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        if (!resolved) {
            try {
                this.value = Integer.parseInt(str_value);
            } catch (NumberFormatException e) {
                throw new Failure(pos, "Invalid Integer Literal: " + this.str_value + ".");
            }
            resolved = true;
        }
        return Type.INT;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        a.emit("movl", a.immed(value), a.reg(free));
    }

    void compileExprOp(Assembly a, String op, int free) {
        a.emit(op, a.immed(value), a.reg(free));
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is false.
     */
    void branchFalse(Assembly a, String lab, int free) {
        if (value == 0) {
            a.emit("jmp", lab);
        }
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is true.
     */
    void branchTrue(Assembly a, String lab, int free) {
        if (value != 0) {
            a.emit("jmp", lab);
        }
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return new IntValue(value);
    }
    public org.llvm.Value llvmGen(LLVM l) {
        return Type.INT.llvmType().constInt(value, false);
    }
}
