// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;
import java.util.Iterator;
import java.util.Collections;
import org.llvm.TypeRef;

/** Provides a representation for local variable declarations in a block.
 */
public class LocalVarDecl extends Statement {
    private Type type;
    private VarDecls varDecls;
    public LocalVarDecl(Position pos, Type type, VarDecls varDecls) {
        super(pos);
        this.type     = type;
        this.varDecls = varDecls;
    }

    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        return check(ctxt, env, frameOffset,
                     Collections.<Statement>emptyList().iterator());
    }
    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset,
                         Iterator<Statement> iter) {
        type = type.check(ctxt);
        if (type != null) {
            int size = type.size();
            for (VarDecls vs = varDecls; vs != null; vs = vs.getNext()) {
                try {
                    if (VarEnv.find(vs.getId().getName(), env) != null) {
                        ctxt.report(new Failure(pos,
                                                "Repeated definition for variable " + vs.getId()));
                    } else if (vs.getInitExpr() != null
                               && !type.isSuperOf(vs.getInitExpr().typeOf(ctxt, env))) {
                        ctxt.report(new Failure(pos, "Incorrect type for initialization expression"));
                    } else {
                        frameOffset -= size;
                        env = new VarEnv(vs.getId(), type, frameOffset, env);
                    }
                } catch (Diagnostic d) {
                    ctxt.report(d);
                }
            }
            ctxt.reserveSpace(frameOffset);
        }
        if (!iter.hasNext()) {
            ctxt.report(new Failure(pos, "Declarations have no use"));
            return true;
        } else {
            Statement s = iter.next();
            return s.check(ctxt, env, frameOffset, iter);
        }
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        throw new RuntimeException("Needs to be implemented to init vars");
    }

    public void llvmGen(LLVM l) {
        org.llvm.Builder b = l.getBuilder();
        org.llvm.Value func = b.getInsertBlock().getParent();

        for (VarDecls vs = varDecls; vs != null; vs = vs.getNext()) {
            TypeRef t = type.llvmType();

            if (type.isClass() != null) {
                t = t.pointerType();
            }

            org.llvm.Value v = b.buildAlloca(t, vs.getId().getName());
            if (vs.getInitExpr() != null) {
                b.buildStore(vs.getInitExpr().llvmGen(l), v);
            } else {
                b.buildStore(type.defaultValue(), v);
            }
            l.setNamedValue(vs.getId().getName(), v);
        }
    }


    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        return null;
    }
}
