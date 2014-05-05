package codegen;
import syntax.*;

public class LLVM {
    private LLVMExprVisitor expr_v;
    private LLVMStmtVisitor stmt_v;

    public void LLVM() {
        expr_v = new LLVMExprVisitor();
        stmt_v = new LLVMStmtVisitor();
    }

    public void emit(String file) {

    }
}
