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


package checker;

import compiler.*;
import syntax.*;
import codegen.*;
import interp.*;
import util.*;
import java.lang.Iterable;
import java.util.Iterator;
/** Provides a representation for object field environments.
 */
public final class FieldEnv extends MemberEnv implements Iterable<FieldEnv>,
        ListIteratorIF<FieldEnv> {
    private FieldEnv next;
    private int      offset;  // offset of field within object (0 for static)
    private int fieldIndex;
    private org.llvm.Value staticField;
    private Expression init_expr;
    public FieldEnv(Modifiers mods, Id id, Type type, ClassType owner,
                    int fieldIndex, int offset, FieldEnv next, Expression init_expr) {
        super(mods, id, type, owner);
        this.offset = offset;
        this.next   = next;
        this.fieldIndex = fieldIndex;
        this.staticField = null;
        this.init_expr = init_expr;
    }
    public Expression getInitExpr() {
        return this.init_expr;
    }
    public void setStaticField(org.llvm.Value field) {
        staticField = field;
    }
    public org.llvm.Value getStaticField() {
        return staticField;
    }
    public Iterator<FieldEnv> iterator() {
        return new ListIterator<FieldEnv>(this);
    }
    public int getOffset() {
        return offset;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public ClassType getOwner() {
        return owner;
    }

    public FieldEnv getNext() {
        return next;
    }

    public FieldEnv setNext(FieldEnv e) {
        next = e;
        return this;
    }
    /** Look for the entry corresponding to a particular identifier
     *  in a given environment.
     */
    public static FieldEnv find(String name, FieldEnv env) {
        while (env != null && !name.equals(env.id.getName())) {
            env = env.next;
        }
        return env;
    }

    /** Check a list of field definitions.
     */
    public static void checkFields(Context ctxt, FieldEnv env) {
        if (env != null) {
            for (FieldEnv f : env) {
                try {
                    if (f.getInitExpr() != null
                        && !f.getType().isSuperOf(f.getInitExpr().typeOf(ctxt, null))) {
                        ctxt.report(new Failure("Type of member initialization does not match." +
                                                f.getName()));
                    }
                } catch (Diagnostic d) {
                    ctxt.report(d);
                }
            }
        }
    }
    /** Construct a printable description of this environment entry for
     *  use in error diagnostics.
     */
    public String describe() {
        return "field " + id;
    }

    /** Compile a list of (global) variable definitions.
     */
    public static void compileFields(Assembly a, FieldEnv env) {
        for (; env != null; env = env.next) {
            env.compileField(a);
        }
    }

    /** Output the code to reserve space for a declared field.  Only the
     *  static fields need space reserved in this way; space for non-static
     *  fields is allocated dynamically as part of object creation.
     */
    void compileField(Assembly a) {
        if (isStatic()) {
            a.emitVar(this, type.size());
        }
    }

    public Statement staticInit() {
        if (isStatic()) {
            Expression init = init_expr;
            Position pos = id.getPos();
            if (init == null) {
                init = new NullLiteral(pos);
            }
            return new ExprStmt(pos, new AssignExpr(pos, new ClassAccess(this), init));
        }
        return null;
    }

    public org.llvm.TypeRef llvmType() {
        return type.llvmType();
    }

    public org.llvm.TypeRef llvmTypeField() {
        return type.llvmTypeField();
    }

    /** Generate code to load the value of a field from an object whose
     *  address is passed in through the current free register.
     */
    public void loadField(Assembly a, int free) {
        if (isStatic()) {
            a.emit("movl", staticName(a), a.reg(free));
        } else {
            a.emit("movl", a.indirect(offset, a.reg(free)), a.reg(free));
        }
    }

    /** Save the value in the free register in a field of the object that
     *  is pointed to by register (free+1).
     */
    public void saveField(Assembly a, int free) {
        if (isStatic()) {
            a.emit("movl", a.reg(free), staticName(a));
        } else {
            a.emit("movl", a.reg(free), a.indirect(offset, a.reg(free + 1)));
        }
    }

    /** Mangle the class and field name to make a label for a global
     *  (static) field.
     */
    public String staticName(Assembly a) {
        return a.mangle("v_" + owner.toString(), getName());
    }

    // For interpreter use only: -------------------
    private Value val = null;  // used for static fields

    public Value getField(ObjValue obj) {
        if (isStatic()) {
            if (val == null) {
                Interp.abort("Attempt to use uninitialized static variable! " + id.getName());
            }
            return val;
        } else {
            return obj.getField(offset);
        }
    }

    public void setField(ObjValue obj, Value val) {
        if (isStatic()) {
            this.val = val;
        } else {
            obj.setField(offset, val);
        }
    }

    public org.llvm.Value llvmField(LLVM l, org.llvm.Value object) {
        if (isStatic()) {
            return staticField;
        } else {
            return l.getBuilder().buildStructGEP(object, fieldIndex, id.getName());
        }
    }
}
