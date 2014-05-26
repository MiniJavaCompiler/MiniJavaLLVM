// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

import org.llvm.Builder;

/** Represents a method invocation through this.
 */
public final class ThisInvocation extends Invocation {
    private String  name;
    private MethEnv menv;
    private int     size;

    public ThisInvocation(Id id, Args args, MethEnv menv) {
        super(id.getPos(), args);
        this.name = id.getName();
        this.menv = menv;
    }

    /** Calculate the type of this method invocation.
     */
    Type typeInvocation(Context ctxt, VarEnv env) throws Diagnostic {
        size = ctxt.getCurrMethod().getSize();
        return checkInvocation(ctxt, env, this.menv);
    }

    /** Generate code for this method invocation, leaving
     *  the result in the specified free variable.
     */
    void compileInvocation(Assembly a, int free) {
        if (!menv.isStatic()) {
            a.loadThis(size, 0);
        }
        menv.compileInvocation(a, args, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        if (menv.isStatic()) {
            return menv.call(st, null, args);
        } else {
            return menv.call(st, st.getThis(size), args);
        }
    }

    public org.llvm.Value llvmGen(LLVM l) {
        Builder b = l.getBuilder();
        org.llvm.Value method_this;
        org.llvm.Value func;
        if (!menv.isStatic()) {
            org.llvm.Value obj = l.getFunction().getParam(0);
            org.llvm.Value vtable_addr =  b.buildStructGEP(obj, 0, "vtable_lookup");
            org.llvm.Value vtable = b.buildLoad(vtable_addr, "vtable");
            org.llvm.Value func_addr = b.buildStructGEP(vtable, menv.getSlot(),
                                       "func_lookup");
            func = b.buildLoad(func_addr, menv.getName());
            method_this = b.buildBitCast(obj, menv.getOwner().llvmType().pointerType(),
                                         "cast_this");
        } else {
            /* static method can just use the existing function name */
            method_this = null;
            func = menv.getFunctionVal(l);
        }

        return llvmInvoke(l, menv, func, method_this);
    }
}
