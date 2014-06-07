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

/** Provides a representation for the empty statement.
 */
public final class Empty extends Statement {
    public Empty(Position pos) {
        super(pos);
    }

    public void llvmGen(LLVM l) {
        /* does nothing */
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        return true;  // Always run on
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        // Nothing to do!
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        return null;
    }
}
