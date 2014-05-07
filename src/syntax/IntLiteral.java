// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for integer literals.
 */
public final class IntLiteral extends Literal {
    private int value;
    public int getValue() {
        return value;
    }
    public IntLiteral(Position pos, int value) {
        super(pos);
        this.value = value;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        return Type.INT;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        a.emit("movl", a.immed(value), a.reg(free));
    }

    void compileExprOp(Assembly a, String op, int free) {
        a.emit(op, a.immed(value), a.reg(free));
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is false.
     */
    void branchFalse(Assembly a, String lab, int free) {
        if (value == 0) {
            a.emit("jmp", lab);
        }
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is true.
     */
    void branchTrue(Assembly a, String lab, int free) {
        if (value != 0) {
            a.emit("jmp", lab);
        }
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return new IntValue(value);
    }
}
