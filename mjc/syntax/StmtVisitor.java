package syntax;

public abstract interface StmtVisitor<T, R> {
    R visit(Block item, T data);
    R visit(DoWhile item, T data);
    R visit(Empty item, T data);
    R visit(ExprStmt item, T data);
    R visit(IfThenElse item, T data);
    R visit(Return item, T data);
    R visit(While item, T data);
}
