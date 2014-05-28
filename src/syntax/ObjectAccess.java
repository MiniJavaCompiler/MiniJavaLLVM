// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Represents an access to an instance field.
 */
public final class ObjectAccess extends FieldAccess {
    private Expression object;
    private String     name;
    private FieldEnv   env;

    public ObjectAccess(Expression object, Id id) {
        super(id.getPos());
        this.object = new NullCheck(pos, object);
        this.name   = id.getName();
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        Type receiver = object.typeOf(ctxt, env);
        ClassType cls = receiver.isClass();
        if (cls == null) {
            throw new Failure(pos,
            "Cannot access field " + name +
            " in a value of type " + receiver);
        } else if ((this.env = cls.findField(name)) == null) {
            throw new Failure(pos,
            "Cannot find field " + name + " in class " + cls);
        }
        this.env.accessCheck(ctxt, pos);
        return this.env.getType();
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        object.compileExpr(a, free);
        env.loadField(a, free);
    }

    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    void saveVar(Assembly a, int free) {
        a.spill(free + 1);
        object.compileExpr(a, free + 1);
        env.saveField(a, free);
        a.unspill(free + 1);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return env.getField(object.eval(st).getObj());
    }

    /** Save a value in the location specified by this left hand side.
     */
    public void save(State st, Value val) {
        env.setField(object.eval(st).getObj(), val);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        org.llvm.Value obj = object.llvmGen(l);
        org.llvm.Value res = l.getBuilder().buildStructGEP(obj, env.getFieldIndex(),
                             env.getName());
        return l.getBuilder().buildLoad(res, env.getName());
    }

    public org.llvm.Value llvmSave(LLVM l, org.llvm.Value r) {
        org.llvm.Value obj = object.llvmGen(l);
        org.llvm.Value field = l.getBuilder().buildStructGEP(obj, env.getFieldIndex(),
                               env.getName());
        return l.getBuilder().buildStore(r, field);
    }
}
