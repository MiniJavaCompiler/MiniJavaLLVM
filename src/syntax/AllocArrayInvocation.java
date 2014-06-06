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
import syntax.*;

import java.util.ArrayList;
import org.llvm.TypeRef;

/** Provides a representation for method invocations.
 */
public class AllocArrayInvocation extends ExternalInvocation {
    private ArrayType arrayType;
    private Type array;
    private Expression size;
    public AllocArrayInvocation(Position pos, Type array, Type elem,
                                Expression size) {
        super(
            new Name(new Name(new Id(pos, "MJC")), new Id(pos, "allocArray")),
            new Args(size,
                     new Args(new TypeLen(pos, elem), null)));
        this.array = array;
        this.size = size;
    }
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        arrayType = array.check(ctxt).isArray();
        if (arrayType == null) {
            throw new Failure("AllocArrayInvocation expects array type");
        }
        return super.typeOf(ctxt, env);
    }
    public Value eval(State st) {
        return new ArrayValue(size.eval(st).getInt(), arrayType);
    }
}
