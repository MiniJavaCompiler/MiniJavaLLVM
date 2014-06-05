package syntax;
import java.util.ArrayList;
import java.lang.reflect.Array;
import syntax.*;

abstract public class MjcList<T> extends ArrayList<T> {
    public static class TypeList extends MjcList<Type> {
        public TypeList(Type x) {
            this();
            append(x);
        }
        public TypeList() {
            super(Type.class);
        }
    }
    public static class StmtList extends MjcList<Statement> {
        public StmtList(Statement x) {
            this();
            append(x);
        }
        public StmtList() {
            super(Statement.class);
        }
    }
    public static class StringLiteralList extends MjcList<StringLiteral> {
        public StringLiteralList(StringLiteral x) {
            this();
            append(x);
        }
        public StringLiteralList() {
            super(StringLiteral.class);
        }
    }
    private Class classType;
    public MjcList(Class c) {
        super();
        classType = c;
    }
    public MjcList<T> append(T x) {
        super.add(x);
        return this;
    }
    public T [] toArray() {
        return super.toArray((T[])Array.newInstance(classType, 0));
    }
}
