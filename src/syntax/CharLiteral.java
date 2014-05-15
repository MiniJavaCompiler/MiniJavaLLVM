package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for String literals.
 */
public final class CharLiteral extends Literal {
    private char value;
    public CharLiteral(Position pos, char literal) {
        super(pos);
        this.value = literal;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        return Type.CHAR;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        throw new RuntimeException("Can't compile char literal yet");
        //a.emit("movl", a.immed(value ? 1 : 0), a.reg(free));
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is false.
     */
    void branchFalse(Assembly a, String lab, int free) {
        /* cannot be false */
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
        return CharValue.make(value);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return Type.CHAR.llvmType().constInt((char)value, false);
    }
}
