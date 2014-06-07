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
import org.llvm.TypeRef;
import codegen.*;

/** Provides a representation for primitive types.
 */
public final class PrimitiveType extends Type {
    private String name;
    public PrimitiveType(String name) {
        this.name = name;
    }

    /** Return a printable representation of this type.
     */
    public String toString() {
        return name;
    }

    public void llvmGenTypes(LLVM l) {
        /* primitive types already exist */
    }

    /** Test for equality with another type.
     */
    public boolean equal(Type type) {
        if (type instanceof PrimitiveType) {
            PrimitiveType that = (PrimitiveType)type;
            return that.name.equals(this.name);
        }
        return false;
    }

    public TypeRef llvmType() {
        if (this.equal(Type.INT)) {
            return TypeRef.int32Type();
        } else if (this.equal(Type.LONG)) {
            return TypeRef.int64Type();
        } else if (this.equal(Type.FLOAT)) {
            return TypeRef.floatType();
        } else if (this.equal(Type.DOUBLE)) {
            return TypeRef.doubleType();
        } else if (this.equal(Type.BOOLEAN)) {
            return TypeRef.int1Type();
        } else if (this.equal(Type.NULL)) {
            /* this should really be the type it's going to be assigned to */
            return TypeRef.int8Type().pointerType();
        } else if (this.equal(Type.VOID)) {
            return TypeRef.voidType();
        } else if (this.equal(Type.STRING)) {
            return TypeRef.int8Type().pointerType();
        } else if (this.equal(Type.CHAR)) {
            return TypeRef.int8Type();
        } else if (this.equal(Type.PTR)) {
            return TypeRef.int8Type().pointerType();
        } else {
            throw new RuntimeException("Unknown LLVM Primitive Type: " + name);
        }
    }

    public int getWidth() {
        return Assembly.WORDSIZE;
    }
    public org.llvm.Value defaultValue() {
        if (this.equal(Type.NULL) || this.equal(Type.PTR)) {
            return llvmType().constPointerNull();
        } else {
            return llvmType().constInt(0, false);
        }
    }

    public Expression defaultExpr(Position pos) {
        if (this.equal(Type.VOID)) {
            throw new RuntimeException("Void does not have a default value");
        } else if (this.equal(Type.NULL) || this.equal(Type.PTR)) {
            return new NullLiteral(pos);
        } else {
            return new CastExpr(pos, this, new IntLiteral(pos, 0));
        }
    }
}
