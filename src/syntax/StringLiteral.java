package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for String literals.
 */
public final class StringLiteral extends Literal {
    private String value;
    org.llvm.Value actual;
    ClassType stringType = null;
    Expression built;
    static int string_lit_temp = 0;

    public String getString() {
        return this.value;
    }
    public void setLLVMString(org.llvm.Value llvm_string) {
        actual = llvm_string;
    }
    public StringLiteral(Position pos, String literal) {
        super(pos);
        this.value = literal;
        this.actual = null;
        this.stringType = null;
        this.built = null;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        ClassType string = ctxt.findClass("String");
        return string;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        throw new RuntimeException("Can't compile string literal yet");
        //a.emit("movl", a.immed(value ? 1 : 0), a.reg(free));
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is false.
     */
    void branchFalse(Assembly a, String lab, int free) {
        /* cannot be false */
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is true.
     */
    void branchTrue(Assembly a, String lab, int free) {
        a.emit("jmp", lab);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return StringValue.make(value);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return actual;
    }
}
