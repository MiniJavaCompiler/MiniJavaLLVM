package codegen;
import syntax.*;

class LLVMStmtVisitor implements StmtVisitor<LLVM, Void> {
    public Void visit(Block e, LLVM llvm) {
        throw new RuntimeException("Unimplemented LLVMStmtVisitor");
    }
    public Void visit(DoWhile e, LLVM llvm) {
        throw new RuntimeException("Unimplemented LLVMStmtVisitor");
    }
    public Void visit(Empty e, LLVM llvm) {
        throw new RuntimeException("Unimplemented LLVMStmtVisitor");
    }
    public Void visit(ExprStmt e, LLVM llvm) {
        throw new RuntimeException("Unimplemented LLVMStmtVisitor");
    }
    public Void visit(IfThenElse e, LLVM llvm) {
        throw new RuntimeException("Unimplemented LLVMStmtVisitor");
    }
    public Void visit(Return e, LLVM llvm) {
        throw new RuntimeException("Unimplemented LLVMStmtVisitor");
    }
    public Void visit(While e, LLVM llvm) {
        throw new RuntimeException("Unimplemented LLVMStmtVisitor");
    }
}
