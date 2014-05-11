package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for expressions that can appear
 *  in a statement.
 */
public class CastExpr extends Expression {
    Expression needsCast;
    Type castType;

    public CastExpr(Position pos, Type castType, Expression needsCast) {
        super(pos);
        this.castType = castType;
        this.needsCast = needsCast;
    }

    /** Type check this expression in places where it is used as a statement.
     *  We override this method in Invocation to deal with methods that
     *  return void.
     */
    void checkExpr(Context ctxt, VarEnv env) throws Diagnostic {
        typeOf(ctxt, env);
    }

    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        needsCast.typeOf(ctxt, env);
        return castType;
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return l.getBuilder().buildBitCast(needsCast.llvmGen(l),
                                           castType.llvmType().pointerType(), "cast");
    }

    public void compileExpr(Assembly a, int free) {
        /* no need for casts in x86 */
        needsCast.compileExpr(a, free);
    }
    public Value eval(State st) {
        return needsCast.eval(st);
    }
}
