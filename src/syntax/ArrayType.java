// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import checker.*;
import compiler.*;
import codegen.*;
import interp.*;
import syntax.*;

import java.util.Arrays;
import org.llvm.TypeRef;
import java.util.ArrayList;
import org.llvm.Builder;

import org.llvm.binding.LLVMLibrary.LLVMLinkage;

/** Provides a representation for class types.
 */
public final class ArrayType extends ClassType {
    private Type elementType;
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
              null);

        this.elementType = elementType;

        Position pos = id.getPos();
        Modifiers m = new Modifiers(id.getPos());
        m.set(Modifiers.PUBLIC);
        Modifiers m2 = new Modifiers(id.getPos());
        m2.set(Modifiers.PRIVATE);
        Id length = new Id(id.getPos(), "length");
        Id array = new Id(id.getPos(), "array");
        Id size = new Id(id.getPos(), "size");
        VarDecls v = new VarDecls(length);
        VarDecls v2 = new VarDecls(array);
        Decls d = new FieldDecl(m, Type.INT, v);
        Decls d2 = new FieldDecl(m2, Type.PTR, v2);

        Statement [] body = {
            new ExprStmt(pos, new AssignExpr(pos, new ObjectAccess(new This(pos), length), new NameAccess(new Name(size)))),
            new ExprStmt(pos, new AssignExpr(pos, new ObjectAccess(new This(pos), array),
            new AllocArrayInvocation(pos, this, new NameAccess(new Name(size)))))
        };

        Decls d3 = new MethDecl(true, m, Type.VOID, buildArrayName(id),
                                new Formals(Type.INT, size),
                                new Block(pos, body));

        d.link(d2);
        d2.link(d3);

        this.decls = d;
    }

    public ArrayType isArray() {
        return this;
    }
}
