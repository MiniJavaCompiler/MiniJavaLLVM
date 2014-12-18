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

/** Provides a representation for "new" expressions that allocate an
 *  instance of a class.
 */
public class NewExpr extends StatementExpr {
    private Name      name;
    private ClassType cls;
    public NewExpr(Position pos, Name name) {
        super(pos);
        this.name = name;
        this.cls  = null;
    }
    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        cls = name.asClass(ctxt);
        if (cls == null) {
            throw new Failure(pos, "Undefined constructor name " + name);
        } else if (cls.getMods().includes(Modifiers.ABSTRACT)) {
            throw new Failure(pos, "Unable to instantiate abstract class or interface " +
            name);
        }
        return cls;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        a.spillAll(free);
        a.emit("movl", a.vtAddr(cls), a.reg(free));
        a.emit("pushl", a.indirect(0, a.reg(free)));
        a.call(a.name("MJC_allocObject"), free, a.WORDSIZE);
        a.emit("movl", a.vtAddr(cls), a.indirect(0, "%eax"));
        a.unspillAll(free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return cls.newObject();
    }

    public org.llvm.Value llvmGen(LLVM l) {
        Builder b = l.getBuilder();
        org.llvm.Value size = b.buildTrunc(cls.llvmType().sizeOf(), Type.INT.llvmType(),
                                           "trunc_size");
        org.llvm.Value [] args = {size};
        org.llvm.Value mem = b.buildCall(l.getGlobalFn(LLVM.GlobalFn.NEW_OBJECT),
                                         "new_obj", args);
        org.llvm.Value res = b.buildBitCast(mem, cls.llvmType().pointerType(),
                                            "obj_cast");
        org.llvm.Value vtable_field = b.buildStructGEP(res, 0, "vtable");
        b.buildStore(cls.getVtableLoc(), vtable_field);
        return res;
    }
}
