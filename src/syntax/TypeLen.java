// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;
import org.llvm.Builder;

/** Provides a representation for "new" expressions that allocate an
 *  instance of a class.
 */
public class TypeLen extends Expression {
    private Type type;
    public TypeLen(Position pos, Type type) {
        super(pos);
        this.type = type;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        return Type.INT;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        a.emit("movl", a.immed(type.getWidth()), a.reg(free));
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return new IntValue(type.getWidth());
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return l.getBuilder().buildTrunc(type.llvmTypeField().sizeOf(),
                                         Type.INT.llvmType(), "typelen_cast");
    }
}
