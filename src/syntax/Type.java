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

import compiler.*;
import checker.*;
import codegen.*;

import org.llvm.TypeRef;

/** Provides a representation for types.
 */
public abstract class Type {
    public static final Type INT     = new PrimitiveType("int");
    public static final Type LONG    = new PrimitiveType("long");
    public static final Type FLOAT   = new PrimitiveType("float");
    public static final Type DOUBLE  = new PrimitiveType("double");
    public static final Type BOOLEAN = new PrimitiveType("boolean");
    public static final Type NULL    = new PrimitiveType("null");
    public static final Type VOID    = new PrimitiveType("void");
    public static final Type STRING  = new PrimitiveType("str_lit");
    public static final Type CHAR    = new PrimitiveType("char");
    public static final Type PTR     = new PrimitiveType("ptr");

    public static Type [] getArrayPrimitives() {
        Type [] prims = {Type.INT, Type.LONG, Type.FLOAT, Type.DOUBLE, Type.BOOLEAN, Type.CHAR};
        return prims;
    }
    public abstract String toString();
    public abstract org.llvm.Value defaultValue();

    /** Test for equality with another type.
     */
    public abstract boolean equal(Type type);

    public abstract TypeRef llvmType();

    public TypeRef llvmTypeField() {
        if (this instanceof PrimitiveType) {
            return llvmType();
        } else {
            return llvmType().pointerType();
        }
    }

    public abstract void llvmGenTypes(LLVM l);

    /** Test to see if this class is a supertype of another type.
     */
    public boolean isSuperOf(Type type) {
        return this.equal(type);
    }

    /** Test to see if this type is a class; by default, we return null.
     */
    public ClassType isClass() {
        return null;
    }

    public ArrayType isArray() {
        return null;
    }

    public InterfaceType isInterface() {
        return null;
    }
    /** Check to ensure that this is a valid type.  This is part of the
     *  mechanism used to deal with types that are specified by name,
     *  which cannot be properly resolved until parsing is complete.
     */
    public Type check(Context ctxt) {
        return this;
    }

    /** Returns the number of bytes needed to store an object of
     *  this type.
     */
    public int size() {
        return Assembly.WORDSIZE;
    }

    abstract public int getWidth();

    public static Type mixedClass(Type l_t, Type r_t) {
        if (l_t.equal(r_t)) {
            return null;
        } else if (l_t.isSuperOf(r_t)) {
            return l_t;
        } else if (r_t.isSuperOf(l_t)) {
            return r_t;
        } else {
            return null;
        }
    }
    public abstract Expression defaultExpr(Position pos);
}
