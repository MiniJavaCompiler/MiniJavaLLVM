// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;

import org.llvm.binding.LLVMLibrary.LLVMIntPredicate;
import org.llvm.binding.LLVMLibrary.LLVMRealPredicate;

/** Provides a representation for relational comparison operators.
 */
public abstract class RelOpExpr extends BinaryOp {
    abstract public LLVMIntPredicate getllvmRelOp();
    public RelOpExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        try {
            Type lt = left.typeOf(ctxt, env);
            Type rt = right.typeOf(ctxt, env);
            if (!lt.equal(rt)) {
                ctxt.report(new Failure(pos,
                "Operands should have the same type, but the " +
                " left operand has type " + lt +
                " and the right operand has type " + rt));
            } else if (Type.mixedNull(lt, rt)) {
                if (lt == Type.NULL) {
                    left = new CastExpr(pos, rt, left);
                } else {
                    right = new CastExpr(pos, lt, right);
                }
            }
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        return Type.BOOLEAN;
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return l.getBuilder().buildICmp(getllvmRelOp(), left.llvmGen(l),
                                        right.llvmGen(l), "cmptmp");
    }
}
