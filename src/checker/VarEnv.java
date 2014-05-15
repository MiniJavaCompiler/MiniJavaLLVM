// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package checker;

import org.llvm.BasicBlock;
import org.llvm.Builder;
import org.llvm.TypeRef;

import compiler.*;
import syntax.*;
import codegen.*;
import interp.*;

/** Provides a representation for variable environments (parameters &
 *  local vars).
 */
public final class VarEnv extends Env {
    private int    offset;
    private VarEnv next;

    public VarEnv(Id id, Type type, int offset, VarEnv next) {
        super(id, type);
        this.offset = offset;
        this.next   = next;
    }

    public VarEnv(Id id, Type type, VarEnv next) {
        this(id, type, 0, next);
    }

    public VarEnv getNext() {
        return next;
    }
    /** Look for the entry corresponding to a particular identifier
     *  in a given environment.
     */
    public static VarEnv find(String name, VarEnv env) {
        while (env != null && !name.equals(env.id.getName())) {
            env = env.next;
        }
        return env;
    }

    /** Check the arguments for an invocation against the formal parameters.
     */
    static void checkArgs(Position pos, Context ctxt, VarEnv env,
                          Args args, VarEnv formals) {
        while (args != null && formals != null) {
            try {
                Type argt = args.getArg().typeOf(ctxt, env);
                Type fort = formals.getType();
                if (!fort.isSuperOf(args.getArg().typeOf(ctxt, env))) {
                    ctxt.report(new Failure(args.getArg().getPos(),
                                            "Cannot use argument of type " + argt +
                                            " where a value of type " + fort +
                                            " is expected"));
                } else if (argt != fort) {
                    args.setArg(new CastExpr(pos, fort, args.getArg()));
                }
            } catch (Diagnostic d) {
                ctxt.report(d);
            }
            args    = args.getNext();
            formals = formals.next;
        }
        if (formals != null) {
            ctxt.report(new Failure(pos, "Too few arguments"));
        } else if (args != null) {
            ctxt.report(new Failure(args.getArg().getPos(),
                                    "Too many arguments"));
        }
    }

    /** Test to see whether two variable environments (representing formal
     *  parameters to a function) have the same types.
     */
    static boolean eqTypes(VarEnv ps, VarEnv qs) {
        while (ps != null && qs != null && ps.type.equal(qs.type)) {
            ps = ps.next;
            qs = qs.next;
        }
        return (ps == null) && (qs == null);
    }

    /** Assign offsets that map the identifiers in this environment
     *  to appropriate locations in a stack frame.
     */
    public static int fitToFrame(VarEnv env) {
        if (env == null) {
            return 0;
        } else {
            int size   = fitToFrame(env.next);
            env.offset = Assembly.FRAMEHEAD + size;
            return size + env.type.size();
        }
    }

    /** Generate code to load the value of this variable from the
     *  stack frame into the next free register.
     */
    public void loadVar(Assembly a, int free) {
        a.emit("movl", a.indirect(offset, "%ebp"), a.reg(free));
    }

    /** Generate code to save the value in the free register into this
     *  variable on the stack frame.
     */
    public void saveVar(Assembly a, int free) {
        a.emit("movl", a.reg(free), a.indirect(offset, "%ebp"));
    }

    /** Return the value of this variable from the current stack frame.
     */
    public Value getFrame(State st) {
        return st.getFrame(offset);
    }

    /** Set the value of this variable on the current stack frame.
     */
    public void setFrame(State st, Value val) {
        st.setFrame(offset, val);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        Builder b = l.getBuilder();
        org.llvm.Value v = l.getNamedValue(getName());

        //org.llvm.Value res = b.buildBitCast(v, TypeRef.int8Type().pointerType(), "gcrootcast");
        //org.llvm.Value [] args = {res, null};
        //org.llvm.Value gc = b.buildCall(l.getGCRoot(), "gcroot", args);
        //b.insertIntoBuilder(gc);
        return b.buildLoad(v, getName());
    }

    public org.llvm.Value llvmSave(LLVM l, org.llvm.Value r) {
        org.llvm.Value v = l.getNamedValue(getName());
        return l.getBuilder().buildStore(r, v);
    }

}
