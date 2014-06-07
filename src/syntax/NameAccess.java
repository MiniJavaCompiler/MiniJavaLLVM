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

/** Represents an access to a field by an as yet unresolved name.
 */
public final class NameAccess extends FieldAccess {
    private Name        name;
    private FieldAccess resolved;

    public NameAccess(Name name) {
        super(name.getPos());
        this.name     = name;
        this.resolved = null;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        resolved = name.asValue(ctxt, env);
        if (resolved == null) {
            throw new Failure(pos, "Undefined name " + name);
        }
        return resolved.typeOf(ctxt, env);
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        resolved.compileExpr(a, free);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return resolved.llvmGen(l);
    }

    public org.llvm.Value llvmSave(LLVM l, org.llvm.Value v) {
        return resolved.llvmSave(l, v);
    }
    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    void saveVar(Assembly a, int free) {
        resolved.saveVar(a, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return resolved.eval(st);
    }

    /** Save a value in the location specified by this left hand side.
     */
    public void save(State st, Value val) {
        resolved.save(st, val);
    }
}
