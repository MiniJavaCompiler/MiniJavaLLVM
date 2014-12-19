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
    static private Id buildArrayName(Position pos, Type t) {
        return new Id(pos, t.toString() + "[]");
    }
    static private Type buildArrayExtends(Position pos) {
        return new NameType(new Name(new Id(pos, "Object")));
    }
    public Type check(Context ctxt) {
        elementType = elementType.check(ctxt);
        return super.check(ctxt);
    }
    static private Decls buildDecls(Position pos, Type elementType) {
        Decls d = null;
        Modifiers m = new Modifiers(pos);
        m.set(Modifiers.PUBLIC);
        Modifiers m2 = new Modifiers(pos);
        m2.set(Modifiers.PRIVATE);
        Id length = new Id(pos, "length");
        Id array = new Id(pos, "array");
        Id size = new Id(pos, "size");
        VarDecls v = new VarDecls(length);
        VarDecls v2 = new VarDecls(array);
        d = new FieldDecl(m, Type.INT, v).link(d);
        d = new FieldDecl(m2, Type.PTR, v2).link(d);

        Type thisType = new NameType(new Name(buildArrayName(pos, elementType)));
        Statement [] body = {
            new ExprStmt(pos, new AssignExpr(pos, new ObjectAccess(new This(pos), length), new NameAccess(new Name(size)))),
            new ExprStmt(pos, new AssignExpr(pos, new ObjectAccess(new This(pos), array),
            new AllocArrayInvocation(pos, thisType, elementType, new NameAccess(new Name(size)))))
        };

        d = new MethDecl(true, m, Type.VOID, buildArrayName(pos, elementType),
                         new Formals(Type.INT, size),
                         new Block(pos, body)).link(d);
        return d;
    }
    public ArrayType(Modifiers mods, Position pos, Type elementType) {
        super(mods,
              buildArrayName(pos, elementType),
              buildArrayExtends(pos),
              new Type[0],
              buildDecls(pos, elementType));
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
        org.llvm.Value array = l.getModule().addGlobal(
                                   elementType.llvmTypeField().arrayType(
                                       elements.length), name + "_array");
        array.setInitializer(org.llvm.Value.constArray(elementType.llvmTypeField(),
                             Arrays.asList(elements)));
        org.llvm.Value ary_ptr = l.getBuilder().buildStructGEP(array, 0, "array_loc");
        args.put("array", l.getBuilder().buildBitCast(ary_ptr, Type.PTR.llvmType(),
                 "ptr_cast"));
        args.put("length", Type.INT.llvmType().constInt(elements.length, true));
        return super.globalInitValue(l, name, args);
    }
}
