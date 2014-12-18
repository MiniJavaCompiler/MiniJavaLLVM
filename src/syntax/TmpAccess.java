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
public final class TmpAccess extends FieldAccess {
    Type type;
    org.llvm.Value llvm_tmp;
    Value interp_tmp;
    int x86_tmp;

    public TmpAccess(Position pos, Type t) {
        super(pos);
        type = t;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        return type.check(ctxt);
    }

    public void setTmp(org.llvm.Value v) {
        llvm_tmp = v;
    }
    public void setTmp(Value v) {
        interp_tmp = v;
    }
    public void setTmp(int free) {
        x86_tmp = free;
    }
    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        a.loadTmp(x86_tmp, free);
    }

    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    void saveVar(Assembly a, int free) {
        a.setTmp(x86_tmp, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return interp_tmp;
    }

    /** Save a value in the location specified by this left hand side.
     */
    public void save(State st, Value val) {
        interp_tmp = val;
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return llvm_tmp;
    }

    public void llvmSave(LLVM l, org.llvm.Value r) {
        l.getBuilder().buildStore(r, llvm_tmp);
    }
}
