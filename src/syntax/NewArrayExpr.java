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
import org.llvm.TypeRef;

/** Provides a representation for "new" expressions that allocate an
 *  instance of a class.
 */
public final class NewArrayExpr extends NewExpr {
    private Expression size;
    public static Name buildArrayExprName(Position pos, Type elementType) {
        return new Name(new Id(pos, elementType.toString() + "[]"));
    }

    public NewArrayExpr(Position pos, Type elementType, Expression size) {
        super(pos, buildArrayExprName(pos, elementType));
        this.size = size;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        if (size.typeOf(ctxt, env).equal(Type.INT)) {
            throw new Failure(pos, "Array size must be of Type INT");
        }
        return super.typeOf(ctxt, env);
    }
}
