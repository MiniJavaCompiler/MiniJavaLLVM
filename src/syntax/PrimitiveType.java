// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import org.llvm.TypeRef;

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
            return TypeRef.int32Type();
        } else if (this.equal(Type.NULL)) {
            //return TypeRef.pointerType();
            throw new RuntimeException("Pointer Type Unhandled");
        } else {
            throw new RuntimeException("Unknown LLVM Primitive Type");
        }
    }
}
