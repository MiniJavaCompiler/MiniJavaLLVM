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
                if (VarEnv.find(vs.getId().getName(), env) != null) {
                    ctxt.report(new Failure(pos,
                                            "Repeated definition for variable " + vs.getId()));
                } else {
                    frameOffset -= size;
                    env = new VarEnv(vs.getId(), type, frameOffset, env);
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
        /* this does nothing in a computational sense */
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
            l.setNamedValue(vs.getId().getName(), v);
            b.buildStore(type.defaultValue(), v);
            
            // set the gcroot for this var for later garbage collection (if it's a pointer)
            if (type.isClass() != null) {
            	org.llvm.Value res = b.buildBitCast(v, TypeRef.int8Type().pointerType().pointerType(), "gctmp");
            	org.llvm.Value meta = TypeRef.int8Type().pointerType().constNull();  // TODO: replace with type data
            	org.llvm.Value [] args = {res, meta};
            	org.llvm.Value gc = b.buildCall(l.getGCRoot(), "", args);  
            }
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
