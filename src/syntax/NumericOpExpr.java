// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;

/** Provides a representation for binary expressions with numeric
 *  operands.
 */
public abstract class NumericOpExpr extends BinaryOp {
    public NumericOpExpr(Position pos, Expression left, Expression right) {
        super(pos, left, right);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        try {
            required(ctxt, "Left operand",  left.typeOf(ctxt, env),  Type.INT);
            required(ctxt, "Right operand", right.typeOf(ctxt, env), Type.INT);
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        return Type.INT;
    }
    public org.llvm.Value llvmGen(LLVM l) {
        org.llvm.Value llvm_left = left.llvmGen(l);
        org.llvm.Value llvm_right = right.llvmGen(l);

        return llvmBuildOp(l, llvm_left, llvm_right);
    }

    public abstract org.llvm.Value llvmBuildOp(LLVM l, org.llvm.Value left,
            org.llvm.Value right);
}
