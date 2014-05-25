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
    ClassType stringType = null;
    ClassType charType = null;
    Expression built;
    static int string_lit_temp = 0;
    private Name name;
    private NameAccess name_access;
    public StringLiteral(Position pos, String literal) {
        super(pos);
        this.value = literal;
        this.actual = null;
        this.stringType = null;
        this.built = null;
        this.name = null;
        this.name_access = null;
    }
    public void setName(Id static_class, Id id) {
        this.name = new Name(new Name(static_class), id);
    }
    public Name getName() {
        return name;
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
        name_access = new NameAccess(name);
        return name_access.typeOf(ctxt, env);
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        name_access.compileExpr(a, free);
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
        name_access.branchTrue(a, lab, free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return name_access.eval(st);
    }

    public org.llvm.Value llvmGen(LLVM l) {
        return name_access.llvmGen(l);
    }
}
