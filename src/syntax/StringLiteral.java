package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for String literals.
 */
public final class StringLiteral extends Literal {
    private String value;
    private org.llvm.Value actual;
    private String x86Name;
    ClassType stringType = null;
    ClassType charType = null;
    Expression built;
    static int string_lit_temp = 0;

    public StringLiteral(Position pos, String literal) {
        super(pos);
        this.value = literal;
        this.actual = null;
        this.stringType = null;
        this.built = null;
    }
    public String getX86Name() {
        return x86Name;
    }

    public void setX86Name(String name) {
        x86Name = name;
    }
    public String getString() {
        return this.value;
    }
    public void setLLVMString(org.llvm.Value llvm_string) {
        actual = llvm_string;
    }
    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        stringType = ctxt.findClass("String");
        charType = ctxt.findClass("char[]");
        return stringType;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        a.emit("movl", a.stringName(this), a.reg(free));
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
        ObjValue v = new ObjValue(stringType, stringType.getWidth());
        v.setField(stringType.findField("string").getOffset(),
                   new ArrayValue(value.length(), charType));
        Value str = v.getField(stringType.findField("string").getOffset());

        for (int x = 0; x < value.length(); x++) {
            str.getArray().setElem(x, new CharValue(value.charAt(x)));
        }
        return v;
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return actual;
    }
}
