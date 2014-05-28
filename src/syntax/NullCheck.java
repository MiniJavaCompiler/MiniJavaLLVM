// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for expressions.
 */
public class NullCheck extends Expression {
    private Expression nullCheck;
    private Expression object;
    private boolean typeResolved;
    public NullCheck(Position pos, Expression obj) {
        super(pos);
        this.typeResolved = false;
        this.object = obj;
        this.nullCheck =
            new NameInvocation(
            new Name(new Name(new Id(pos, "Object")), new Id(pos, "nullCheck")),
            new Args(new StringLiteral(pos, pos.getFilename()),
                     new Args(new IntLiteral(pos, pos.getRow()),
                              new Args(obj, null))));
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return nullCheck.llvmGen(l);
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        if (!typeResolved) {
            Type f = nullCheck.typeOf(ctxt, env);
            Type t = object.typeOf(ctxt, env);
            if (!f.equals(ctxt.findClass("Object"))) {
                throw new Failure(pos, "Object.nullCheck must return type Object (returns " + f
                + ")");
            } else {
                /* this is a very safe upcast */
                nullCheck = new CastExpr(pos, t, nullCheck);
            }
            typeResolved = true;
        }
        return nullCheck.typeOf(ctxt, env);
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        nullCheck.compileExpr(a, free);
    }
    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return nullCheck.eval(st);
    }
}
