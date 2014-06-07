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

/** Represents an access to a class variable.
 */
public final class ClassAccess extends FieldAccess {
    private FieldEnv env;

    public ClassAccess(FieldEnv env) {
        super(env.getPos());
        this.env  = env;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        if (!this.env.isStatic()) {
            throw new Failure(pos,
            "Cannot access field " + this.env.getName() +
            " without an object of class " + this.env.getOwner());
        }
        this.env.accessCheck(ctxt, pos);
        return this.env.getType();
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        env.loadField(a, free);
    }

    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    void saveVar(Assembly a, int free) {
        env.saveField(a, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return env.getField(null);  // static analysis ensures object not used
    }

    /** Save a value in the location specified by this left hand side.
     */
    public void save(State st, Value val) {
        env.setField(null, val);  // static analysis ensures object not used
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return l.getBuilder().buildLoad(env.llvmField(l, null), env.getName());
    }

    public org.llvm.Value llvmSave(LLVM l, org.llvm.Value r) {
        return l.getBuilder().buildStore(r, env.llvmField(l, null));
    }
}
