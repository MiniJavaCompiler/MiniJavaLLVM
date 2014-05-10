// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for unary Boolean not.
 */
public final class NotExpr extends UnaryOp {
    public NotExpr(Position pos, Expression expr) {
        super(pos, expr);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        try {
            required(ctxt, "operand", expr.typeOf(ctxt, env), Type.BOOLEAN);
        } catch (Diagnostic d) {
            ctxt.report(d);
        }
        return Type.BOOLEAN;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        expr.compileExpr(a, free);
        a.emit("xorl", a.immed(1), a.reg(free));
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return BoolValue.make(!expr.eval(st).getBool());
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return l.getBuilder().buildNot(expr.llvmGen(l), "not_temp");
    }
}
