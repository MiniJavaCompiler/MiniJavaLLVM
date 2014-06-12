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

/** Represents an access to a field through a simple identifier.
 */
public final class SimpleAccess extends FieldAccess {
    private FieldEnv env;
    private int      size;

    public SimpleAccess(Id id, FieldEnv env) {
        super(id.getPos());
        this.env  = env;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        this.env.accessCheck(ctxt, pos);
        size = ctxt.getCurrMethod().getSize();
        return this.env.getType();
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        if (!env.isStatic()) {
            a.loadThis(size, free);
        }
        env.loadField(a, free);
    }

    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    void saveVar(Assembly a, int free) {
        if (env.isStatic()) {
            env.saveField(a, free);    // No need to load this for static
        } else {
            a.spill(free + 1);
            a.loadThis(size, free + 1);
            env.saveField(a, free);
            a.unspill(free + 1);
        }
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        // TODO: rewrite this to avoid the test on env.
        ObjValue obj = env.isStatic() ? null : st.getThis(size);
        return env.getField(obj);
    }

    /** Save a value in the location specified by this left hand side.
     */
    public void save(State st, Value val) {
        ObjValue obj = env.isStatic() ? null : st.getThis(size);
        env.setField(obj, val);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return env.llvmLoad(l, env.isStatic() ? null : l.getFunction().getParam(0));
    }

    public void llvmSave(LLVM l, org.llvm.Value v) {
        env.llvmStore(l, v, env.isStatic() ? null : l.getFunction().getParam(0));
    }
}
