// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package checker;

import compiler.*;
import syntax.*;
import codegen.*;
import interp.*;

/** Provides a representation for method environments.
 */
public final class MethEnv extends MemberEnv {
    private VarEnv    params;
    private Statement body;
    private int       slot;        // virtual function table slot number
    private int       size;        // #bytes of parameters
    private int       localBytes;  // #bytes of locals

    private MethEnv   next;

    public MethEnv(Modifiers mods, Type type, Id id, VarEnv params,
                   Statement body, ClassType owner, int slot, int size,
                   MethEnv next) {
        super(mods, id, type, owner);
        this.params = params;
        this.slot   = slot;
        this.size   = size;
        this.body   = body;
        this.next   = next;
    }
    public Statement getBody() {
        return body;
    }
    public MethEnv getNext() {
        return next;
    }
    /** Look for the entry corresponding to a particular identifier
     *  in a given environment.
     */
    public static MethEnv find(String name, MethEnv env) {
        while (env != null && !name.equals(env.id.getName())) {
            env = env.next;
        }
        return env;
    }

    /** Check the arguments for an invocation against the formal parameters.
     */
    public void checkArgs(Position pos, Context ctxt, VarEnv env, Args args) {
        VarEnv.checkArgs(pos, ctxt, env, args, params);
    }

    /** Test to see whether another signature matches the signature of this
     *  method.
     */
    public boolean eqSig(Type type, VarEnv params) {
        return ((this.type == null && type == null) ||
                (this.type != null && type != null && this.type.equal(type))) &&
               VarEnv.eqTypes(this.params, params);
    }

    /** Static analysis on a list of method definitions.
     */
    public static void checkMethods(Context ctxt, MethEnv menv) {
        for (; menv != null; menv = menv.next) {
            menv.checkMethod(ctxt);
        }
    }

    /** Static analysis on a method body
     */
    void checkMethod(Context ctxt) {
        if (body != null) {
            ctxt.setCurrMethod(this);
            if (body.check(ctxt, params, 0) && getType() != null) {
                ctxt.report(new Failure(body.getPos(),
                                        "Method does not return a value"));
            }
            localBytes = ctxt.getLocalBytes();
            ctxt.setCurrMethod(null);
        }
    }

    /** Construct a printable description of this environment entry for
     *  use in error diagnostics.
     */
    public String describe() {
        return "method " + id + "(" + ")";
    }

    /** Returns the slot number for this method in the vtable.
     */
    public int getSlot() {
        // TODO: Eliminate this getter
        return slot;
    }

    /** Returns the size (# bytes) of the parameters to this method.
     */
    public int getSize() {
        return size;
    }

    /** Returns the number of bytes used for local variables in this method.
     */
    public int getLocals() {
        return localBytes;
    }

    /** Add entries from this method environment to the vtable for the
     *  enclosing class.
     */
    public static void addToVTable(MethEnv env, MethEnv[] vtable) {
        for (; env != null; env = env.next) {
            if (env.slot >= 0) {
                vtable[env.slot] = env;
            }
        }
    }

    /** Generate the code for a list of method bodies.
     */
    public static void compileMethods(Assembly a, MethEnv menv) {
        for (; menv != null; menv = menv.next) {
            menv.compileMethod(a);
        }
    }

    /** Generate the code for a method body.
     */
    public void compileMethod(Assembly a) {
        if (body != null) {
            a.emitPrologue(methName(a), localBytes);
            body.compileRet(a);
        }
    }

    /** Generate code for a call to this method, assuming that the receiving
     *  object, if any, is in register zero, and also returning the result,
     *  if any, in the specified free register.  We assume also that any other
     *  registers that were in use have been spilled onto the stack before
     *  this method is invoked.
     */
    public void compileInvocation(Assembly a, Args args, int free) {
        int argReg = 0;
        if (!isStatic()) {
            a.emit("pushl", a.reg(0));
            a.emit("movl", a.indirect(0, a.reg(0)), a.reg(0));
            a.emit("movl", a.indirect(a.vtOffset(slot), a.reg(0)), a.reg(0));
            argReg = 1;
        }
        for (; args != null; args = args.getNext()) {
            args.getArg().compileExpr(a, argReg);
            a.emit("pushl", a.reg(argReg));
        }
        if (isStatic()) {
            a.call(methName(a), free, size);
        } else {
            a.call(a.aindirect(a.reg(0)), free, size);
        }
    }

    /** Mangle the class and method name to make a label for the entry
     *  point to this method.
     */
    public String methName(Assembly a) {
        return a.mangle(owner.toString(), getName());
    }

    /** Call this method with a particular object and set of arguments.
     */
    public Value call(State st, ObjValue obj, Args args) {
        if (isStatic()) {
            Args.push(st, args);
            return st.call(this);
        } else {
            obj.checkNull();
            st.push(obj);
            Args.push(st, args);
            return obj.call(st, slot);
        }
    }

    /** Execute a particular method, assuming that the stack frame
     *  has already been set up as necessary.  If the method body
     *  is null, we check to see if there is a corresponding primitive
     *  function and execute that instead.  For the time being, the
     *  only primitive is System.out.
     */
    public Value run(State st) {
        if (body != null) {
            return body.exec(st);
        }
        if (owner.toString().equals("System")
            && getName().equals("out")
            && isStatic()
            && size == 4) {
            System.out.println("out: " + st.getFrame(8).getInt());
            return Value.NULL;
        }
        Interp.abort("Cannot execute method " + getName()
                     + " in class " + owner);
        return null;
    }

}
