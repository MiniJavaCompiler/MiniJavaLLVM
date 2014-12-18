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

/** Represents an access to a method by an as yet unresolved name.
 */
public class NameInvocation extends Invocation {
    private Name       name;
    private Invocation resolved;

    public NameInvocation(Name name, Args args) {
        super(name.getPos(), args);
        this.name     = name;
        this.resolved = null;
    }

    /** Calculate the type of this method invocation.
     */
    Type typeInvocation(Context ctxt, VarEnv env)
    throws Diagnostic {
        resolved = name.asMethod(ctxt, env, args);
        if (resolved == null) {
            throw new Failure(pos, "Undefined method name " + name);
        }
        return resolved.typeInvocation(ctxt, env);
    }

    /** Generate code for this method invocation, leaving
     *  the result in the specified free variable.
     */
    void compileInvocation(Assembly a, int free) {
        resolved.compileInvocation(a, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return resolved.eval(st);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return resolved.llvmGen(l);
    }
}
