package checker;

public abstract interface EnvVisitor<T, R> {
    R visit(FieldEnv item, T data);
    R visit(MethEnv item, T data);
    R visit(VarEnv item, T data);
}
