// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for unary negation.
 */
public final class NegExpr extends UnaryOp {
    public NegExpr(Position pos, Expression expr) {
        super(pos, expr);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        try {
            required(ctxt, "operand", expr.typeOf(ctxt, env), Type.INT);
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        return Type.INT;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        expr.compileExpr(a, free);
        a.emit("negl", a.reg(free));
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return new IntValue(-expr.eval(st).getInt());
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return l.getBuilder().buildNeg(expr.llvmGen(l), "neg_temp");
    }

}
