package syntax;

public abstract interface ExprVisitor<T, R> {
    R visit(AddExpr item, T data);
    R visit(AssignExpr item, T data);
    R visit(BitAndExpr item, T data);
    R visit(BitOrExpr item, T data);
    R visit(BitXorExpr item, T data);
    R visit(BooleanLiteral item, T data);
    R visit(ClassAccess item, T data);
    R visit(ClassInvocation item, T data);
    R visit(CondAndExpr item, T data);
    R visit(CondOrExpr item, T data);
    R visit(DivExpr item, T data);
    R visit(EqualExpr item, T data);
    R visit(FrameAccess item, T data);
    R visit(GreaterThanExpr item, T data);
    R visit(IntLiteral item, T data);
    R visit(LessThanExpr item, T data);
    R visit(MulExpr item, T data);
    R visit(NameAccess item, T data);
    R visit(NameInvocation item, T data);
    R visit(NegExpr item, T data);
    R visit(NewExpr item, T data);
    R visit(NotEqualExpr item, T data);
    R visit(NotExpr item, T data);
    R visit(NullLiteral item, T data);
    R visit(ObjectAccess item, T data);
    R visit(ObjectInvocation item, T data);
    R visit(SimpleAccess item, T data);
    R visit(SuperAccess item, T data);
    R visit(SuperInvocation item, T data);
    R visit(This item, T data);
    R visit(ThisInvocation item, T data);
}
