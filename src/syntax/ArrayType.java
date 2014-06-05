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
import java.util.Hashtable;

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

    static private Decls buildDecls(Id id, Type elementType) {
        Decls d = null;
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
        d = new FieldDecl(m, Type.INT, v).link(d);
        d = new FieldDecl(m2, Type.PTR, v2).link(d);

        Type thisType = new NameType(new Name(buildArrayName(id)));
        Statement [] body = {
            new ExprStmt(pos, new AssignExpr(pos, new ObjectAccess(new This(pos), length), new NameAccess(new Name(size)))),
            new ExprStmt(pos, new AssignExpr(pos, new ObjectAccess(new This(pos), array),
            new AllocArrayInvocation(pos, thisType, elementType, new NameAccess(new Name(size)))))
        };

        d = new MethDecl(true, m, Type.VOID, buildArrayName(id),
                         new Formals(Type.INT, size),
                         new Block(pos, body)).link(d);

        Id index = new Id(pos, "index");
        Id element = new Id(pos, "element");
        Statement [] initElem_body = {
            new ExprStmt(pos, new AssignExpr(pos,
            new ArrayAccess(pos, new This(pos), new NameAccess(new Name(index)), true),
            new NameAccess(new Name(element)))),
            new Return(pos, new This(pos)),
        };

        /* formals in reverse order */
        Formals initElemFormals = new Formals(elementType, element);
        initElemFormals = initElemFormals.link(new Formals(Type.INT, index));
        d = new MethDecl(false, m, thisType, new Id(pos, "initElem"),
                         initElemFormals,
                         new Block(pos, initElem_body)).link(d);
        return d;
    }
    public ArrayType(Modifiers mods, Id id, Type elementType) {
        super(mods,
              buildArrayName(id),
              buildArrayExtends(id),
              new Type[0],
              buildDecls(id, elementType));
        this.elementType = elementType;
    }

    public ArrayType isArray() {
        return this;
    }

    public boolean isSuperOf(Type t) {
        if (t.equal(Type.NULL)) {
            return true;
        }
        ArrayType that = t.isArray();
        if (that != null) {
            Type this_elem = elementType;
            Type that_elem = that.getElementType();
            return this_elem.isSuperOf(that_elem);
        } else {
            return false;
        }
    }

    public void globalInitValue(Assembly a, String name, String [] elements) {
        Hashtable<String, String> args = new Hashtable<String, String>();
        String array_name = name + "_array";
        a.emitLabel(array_name);
        for (String s : elements) {
            a.emit(".long", s);
        }
        args.put("length", Integer.toString(elements.length));
        args.put("array", array_name);
        super.globalInitValue(a, name, args);
    }

    public org.llvm.Value globalInitValue(LLVM l, String name,
                                          org.llvm.Value [] elements) {
        Hashtable<String, org.llvm.Value> args = new
        Hashtable<String, org.llvm.Value>();
        org.llvm.Value array = l.getModule().addGlobal(TypeRef.int64Type().arrayType(
                                   elements.length), name + "_array");
        array.setInitializer(org.llvm.Value.constArray(TypeRef.int64Type(),
                             Arrays.asList(elements)));
        org.llvm.Value ary_ptr = l.getBuilder().buildStructGEP(array, 0, "array_loc");
        args.put("array", l.getBuilder().buildBitCast(ary_ptr, Type.PTR.llvmType(),
                 "ptr_cast"));
        args.put("length", Type.INT.llvmType().constInt(elements.length, true));
        return super.globalInitValue(l, name, args);
    }
}
