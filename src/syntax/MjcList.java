/*
 * MiniJava Compiler - X86, LLVM Compiler/Interpreter for MiniJava.
 * Copyright (C) 2014, 2008 Mitch Souders, Mark A. Smith, Mark P. Jones
 *
 * MiniJava Compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * MiniJava Compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MiniJava Compiler; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
