// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import org.llvm.TypeRef;
import codegen.*;
import org.llvm.Value;
import org.llvm.Dwarf;

/** Provides a representation for primitive types.
 */
public final class PrimitiveType extends Type {
    private String name;
    private org.llvm.Value metaData;

    public PrimitiveType(String name) {
        this.name = name;
        this.metaData = null;
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
        if (this.equal(Type.NULL)) {
            return llvmType().constPointerNull();
        } else {
            return llvmType().constInt(0, false);
        }
    }

    public org.llvm.Value llvmMetaData() {
        if (metaData == null) {
            Value [] metaDataOperands;
            if (this.equal(Type.INT)) {
                metaDataOperands = new org.llvm.Value[] {
                    Dwarf.DW_TAG.DW_TAG_base_type.value(),        //Tag
                    Value.MDString(""),              // File
                    Value.MDString(""),              // Context
                    Value.MDString("int"),  // Name
                    TypeRef.int32Type().constInt(0, false),  // Line number
                    TypeRef.int32Type().constInt(32, false), // Size in Bits
                    TypeRef.int64Type().constInt(32, false), // Align in Bits
                    TypeRef.int64Type().constInt(0, false),  // Offset in Bits
                    TypeRef.int32Type().constInt(0, false),  // Flags
                    Dwarf.DW_ATE.DW_ATE_signed.value()       // Encoding

                };
            } else if (this.equal(Type.LONG)) {
                metaDataOperands = new org.llvm.Value[] {
                    Dwarf.DW_TAG.DW_TAG_base_type.value(),        //Tag
                    Value.MDString(""),              // File
                    Value.MDString(""),              // Context
                    Value.MDString("long"),  // Name
                    TypeRef.int32Type().constInt(0, false),   // Line number
                    TypeRef.int32Type().constInt(64, false),  // Size in Bits
                    TypeRef.int64Type().constInt(64, false),  // Align in Bits
                    TypeRef.int64Type().constInt(0, false),   // Offset in Bits
                    TypeRef.int32Type().constInt(0, false),   // Flags
                    Dwarf.DW_ATE.DW_ATE_signed.value()        // Encoding

                };
            } else if (this.equal(Type.FLOAT)) {
                metaDataOperands = new org.llvm.Value[] {
                    Dwarf.DW_TAG.DW_TAG_base_type.value(),        //Tag
                    Value.MDString(""),              // File
                    Value.MDString(""),              // Context
                    Value.MDString("float"),  // Name
                    TypeRef.int32Type().constInt(0, false),  // Line number
                    TypeRef.int32Type().constInt(32, false), // Size in Bits
                    TypeRef.int64Type().constInt(32, false), // Align in Bits
                    TypeRef.int64Type().constInt(0, false),  // Offset in Bits
                    TypeRef.int32Type().constInt(0, false),  // Flags
                    Dwarf.DW_ATE.DW_ATE_float.value()        // Encoding
                };
            } else if (this.equal(Type.DOUBLE)) {
                metaDataOperands = new org.llvm.Value[] {
                    Dwarf.DW_TAG.DW_TAG_base_type.value(),        //Tag
                    Value.MDString(""),              // File
                    Value.MDString(""),              // Context
                    Value.MDString("double"),  // Name
                    TypeRef.int32Type().constInt(0, false),   // Line number
                    TypeRef.int32Type().constInt(64, false),  // Size in Bits
                    TypeRef.int64Type().constInt(64, false),  // Align in Bits
                    TypeRef.int64Type().constInt(0, false),   // Offset in Bits
                    TypeRef.int32Type().constInt(0, false),   // Flags
                    Dwarf.DW_ATE.DW_ATE_float.value()        // Encoding
                };
            } else if (this.equal(Type.BOOLEAN)) {
                metaDataOperands = new org.llvm.Value[] {
                    Dwarf.DW_TAG.DW_TAG_base_type.value(),        //Tag
                    Value.MDString(""),              // File
                    Value.MDString(""),              // Context
                    Value.MDString("bool"),  // Name
                    TypeRef.int32Type().constInt(0, false),             // Line number
                    TypeRef.int32Type().constInt(8, false),             // Size in Bits
                    TypeRef.int64Type().constInt(8, false),             // Align in Bits
                    TypeRef.int64Type().constInt(0, false),             // Offset in Bits
                    TypeRef.int32Type().constInt(0, false),             // Flags
                    Dwarf.DW_ATE.DW_ATE_boolean.value()                 // Encoding
                };
            } else if (this.equal(Type.NULL)) {
                /* this should really be the type it's going to be assigned to */
                return Type.PTR.llvmMetaData();
            } else if (this.equal(Type.VOID)) {
                throw new RuntimeException("Void type has no metadata");
            } else if (this.equal(Type.STRING)) {
                throw new RuntimeException("String type has no metadata");
            } else if (this.equal(Type.CHAR)) {
                metaDataOperands = new org.llvm.Value[] {
                    Dwarf.DW_TAG.DW_TAG_base_type.value(),        //Tag
                    Value.MDString(""),              // File
                    Value.MDNode(new Value[]{TypeRef.int32Type().constInt(0, false)}),              // Context
                    Value.MDString("char"),  // Name
                    TypeRef.int32Type().constInt(0, false),             // Line number
                    TypeRef.int32Type().constInt(8, false),             // Size in Bits
                    TypeRef.int64Type().constInt(8, false),             // Align in Bits
                    TypeRef.int64Type().constInt(0, false),             // Offset in Bits
                    TypeRef.int32Type().constInt(0, false),             // Flags
                    Dwarf.DW_ATE.DW_ATE_unsigned_char.value()           // Encoding
                };
            } else if (this.equal(Type.PTR)) {
                metaDataOperands = new org.llvm.Value[] {
                    Dwarf.DW_TAG.DW_TAG_pointer_type.value(),        //Tag
                    Value.MDString(""),              // File
                    Value.MDString(""),              // Context
                    Value.MDString("cptr"),  // Name
                    TypeRef.int32Type().constInt(0, false),             // Line number
                    TypeRef.int64Type().constInt(64, false),            // Size in Bits
                    TypeRef.int64Type().constInt(64, false),            // Align in Bits
                    TypeRef.int64Type().constInt(0, false),             // Offset in Bits
                    TypeRef.int32Type().constInt(0, false),             // Flags
                    Type.CHAR.llvmMetaData(),
                };
            } else {
                throw new RuntimeException("Unknown LLVM Primitive Type: " + name);
            }
            metaData = Value.MDNode(metaDataOperands);
        }
        return metaData;
    }
}
