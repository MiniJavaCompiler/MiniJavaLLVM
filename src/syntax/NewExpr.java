// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

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
            throw new Failure(pos, "Undefined name " + name);
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
                                           "cast");
        org.llvm.Value [] args = {size};
        org.llvm.Value mem = b.buildCall(l.getGlobalFn(LLVM.GlobalFn.NEW_OBJECT),
                                         "new_obj", args);
        org.llvm.Value res = b.buildBitCast(mem, cls.llvmType().pointerType(), "cast");
        org.llvm.Value vtable_field = b.buildStructGEP(res, 0, "vtable");
        b.buildStore(cls.getVtableLoc(), vtable_field);
        return res;
    }
}
