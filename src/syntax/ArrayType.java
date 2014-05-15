// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import checker.*;
import compiler.*;
import codegen.*;
import interp.*;

import java.util.Arrays;
import org.llvm.TypeRef;
import java.util.ArrayList;
import org.llvm.Builder;

import org.llvm.binding.LLVMLibrary.LLVMLinkage;

/** Provides a representation for class types.
 */
public final class ArrayType extends ClassType {
    private Type elementType;
    static private Decls buildArrayDecls(Id id, Type elementType) {
        Modifiers m = new Modifiers(id.getPos());
        m.set(Modifiers.PUBLIC);
        VarDecls v = new VarDecls(new Id(id.getPos(), "length"));
        VarDecls v2 = new VarDecls(new Id(id.getPos(), "array"));
        Decls d = new FieldDecl(m, Type.INT, v);
        Decls d2 = new FieldDecl(m, Type.NULL, v2);
        d.link(d2);
        return d;

    }
    public Type getElementType() {
        return elementType;
    }
    static private Id buildArrayName(Id id) {
        return new Id(id.getPos(), id.getName() + "[]");
    }
    static private Type buildArrayExtends(Id id) {
        return new NameType(new Name(new Id(id.getPos(), "Object")));
    }
    public ArrayType(Modifiers mods, Id id, Type elementType) {
        super(mods,
              buildArrayName(id),
              buildArrayExtends(id),
              buildArrayDecls(id, elementType));
        this.elementType = elementType;
    }

    public ArrayType isArray() {
        return this;
    }

}
