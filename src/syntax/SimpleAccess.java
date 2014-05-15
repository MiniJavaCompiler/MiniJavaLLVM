// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Represents an access to a field through a simple identifier.
 */
public final class SimpleAccess extends FieldAccess {
    private String   name;
    private FieldEnv env;
    private int      size;

    public SimpleAccess(Id id, FieldEnv env) {
        super(id.getPos());
        this.name = id.getName();
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
        org.llvm.Value field;
        if (env.getFieldIndex() != -1) {
            org.llvm.Value obj = l.getNamedValue("this");
            org.llvm.Value deref = l.getBuilder().buildLoad(obj, "*this");
            field = l.getBuilder().buildStructGEP(deref, env.getFieldIndex(),
                                                  env.getName());
        } else {
            field = l.getNamedValue(env.getOwner() + "." + env.getName());
        }
        return l.getBuilder().buildLoad(field, env.getName());
    }

    public org.llvm.Value llvmSave(LLVM l, org.llvm.Value v) {
        org.llvm.Value field;
        if (env.getFieldIndex() != -1) {
            org.llvm.Value obj = l.getNamedValue("this");
            org.llvm.Value deref = l.getBuilder().buildLoad(obj, "*this");
            field = l.getBuilder().buildStructGEP(deref, env.getFieldIndex(),
                                                  env.getName());
        } else {
            field = l.getNamedValue(env.getOwner() + "." + env.getName());
        }
        return l.getBuilder().buildStore(v, field);
    }
}
