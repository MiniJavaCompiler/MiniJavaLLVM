package syntax;

public abstract interface TypeVisitor<T, R> {
    R visit(ClassType item, T data);
    R visit(NameType item, T data);
    R visit(PrimitiveType item, T data);
}
