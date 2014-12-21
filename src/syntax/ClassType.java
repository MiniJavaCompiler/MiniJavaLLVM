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
public class ClassType extends Type {
    private static int globalClassId = 0;
    private Modifiers mods;
    private Id        id;
    private Type      extendsType;
    private Type[]    implementsType;
    private Decls     decls;
    private FieldEnv  fields;
    private MethEnv   methods;
    private int       width;    // # bytes for objects of this class
    private int       vfuns;    // # entries in vtable
    private MethEnv[] vtable;   // virtual function table for this class
    private TypeRef llvmType;
    private TypeRef llvmVtable;
    private org.llvm.Value llvmVtableLoc;
    private int classId;
    private int fieldCount;
    public ClassType(Modifiers mods, Id id, Type extendsType, Type[] implementsType,
                     Decls decls) {
        this.mods        = mods;
        this.id          = id;
        this.extendsType = null;
        this.implementsType = implementsType;
        if (extendsType != null) {
            this.extendsType = extendsType;
        } else if (!id.getName().equals("Object")) {
            this.extendsType = new NameType(new Name(new Id(id.getPos(), "Object")));
        }
        this.decls    = decls;
        this.llvmType = null;
        this.llvmVtable = null;
        this.fieldCount = 1;  /* always reserve one spot for vtable */
        this.classId = globalClassId++;
    }

    public int getClassId() {
        return classId;
    }
    public Modifiers getMods() {
        return mods;
    }

    public org.llvm.Value getVtableLoc() {
        return llvmVtableLoc;
    }

    public FieldEnv getFields() {
        return fields;
    }
    public MethEnv getMethods() {
        return methods;
    }

    public int getWidth() {
        return width;
    }

    public MethEnv[] getVtable() {
        return vtable;
    }

    /** Return a printable representation of this type.
     */
    public String toString() {
        return id.toString();
    }

    /** Return the identifier that names this class.
     *
     */
    public Id getId() {
        return id;
    }

    /** Return the position at which the definition for this class appears.
     */
    public Position getPos() {
        return id.getPos();
    }

    /** Return the name of the vtable for this class.
     */
    public String getVTName() {
        return id.getName() + "_0";
    }

    /** Test for equality with another type.
     */
    public boolean equal(Type type) {
        if (type instanceof ClassType) {
            ClassType that = (ClassType)type;
            return that.id.sameId(this.id);
        }
        return false;
    }

    /** Test to see if this type is a class; for ClassTypes, we
     *  just return "this".
     */
    public ClassType isClass() {
        return this;
    }
    private static final int CHECKING = 0;
    private static final int UNCHECKED = (-1);
    private static final int LOWEST = 1;
    private int level = UNCHECKED;

    /** Check this class definition to make sure that there are no
     *  cycles in the class hierarchy, and to process (but not yet
     *  check) any definitions that it contains.
     */
    public void checkClass(Context ctxt)
    throws Diagnostic {
        if (level == CHECKING) {
            /* cannot proceed after this since many method searches rely on being able to terminate */
            throw new Failure(id.getPos(),
            "Cyclic class hierarchy for class " + id);
        } else if (level == UNCHECKED) {
            ClassType extendsClass = null;
            if (extendsType != null) {
                extendsType = extendsType.check(ctxt);
            }
            if (extendsType != null) {
                if (this.equal(extendsType)) {
                    /* cannot proceed after this since many method searches rely on being able to terminate */
                    throw new Failure(id.getPos(),
                                      "Class " + id + " extends itself!");
                }
                extendsClass = extendsType.isClass();
                if (extendsClass == null) {
                    extendsType = null;
                    ctxt.report(new Failure(id.getPos(),
                                            "Illegal superclass"));
                } else {
                    level = CHECKING;
                    extendsClass.checkClass(ctxt);
                    level = 1 + extendsClass.level;
                    width = extendsClass.width;
                    fieldCount = extendsClass.fieldCount;
                    vfuns = extendsClass.vfuns;
                }
            } else {
                level = LOWEST;
                width = Assembly.WORDSIZE; // storage for vptr
                vfuns = 0;
            }

            // Now go on to check the members of the class, secure in the
            // knowledge that the superclass has already been done ...
            for (; decls != null; decls = decls.getNext()) {
                decls.addToClass(ctxt, this);
            }
            int constructor_count = 0;
            for (MethEnv menv = methods; menv != null; menv = menv.getNext()) {
                if (menv.isConstructor()) {
                    constructor_count++;
                }
            }

            boolean has_constructor = constructor_count != 0;

            if (!has_constructor && this.isInterface() == null) {
                /* if no constructor, add a default with nothing to it */
                Position pos = getPos();
                Modifiers m = new Modifiers(pos);
                m.set(Modifiers.PUBLIC);
                Statement maybe_cons = new Empty(pos);
                if (extendsClass != null) {
                    maybe_cons = new ExprStmt(getPos(), new SuperInvocation(getPos(), null, null));
                }
                MethDecl new_cons  = new MethDecl(true, m, this, getId(), null, new Block(pos,
                                                  new Statement[0]));
                new_cons.addToClass(ctxt, this);
            }
            ArrayList<Statement> init_stmts = new ArrayList<Statement>();
            if (fields != null) {
                for (FieldEnv f : fields) {
                    if (!f.isStatic() && f.getInitExpr() != null) {
                        init_stmts.add(new ExprStmt(f.getPos(),
                                                    new AssignExpr(f.getPos(),
                                                            new ObjectAccess(new This(f.getPos()), f.getId()), f.getInitExpr())));

                    }
                }
            }
            Position pos = id.getPos();
            if (isInterface() == null) {
                init_stmts.add(new ExprStmt(pos,
                                            new AssignExpr(pos, new ObjectAccess(new This(pos), new Id(pos, "classId")),
                                                    new IntLiteral(pos, classId))));
            }

            Statement init_block = new Block(id.getPos(),
                                             init_stmts.toArray(new Statement[0]));
            for (MethEnv menv = methods; menv != null; menv = menv.getNext()) {
                if (menv.isConstructor()) {
                    /* add non-static initialization to constructor */
                    StatementExpr super_cons = menv.removeSuperConstructor();
                    MethEnv m = null;
                    Statement maybe_cons = null;
                    if (super_cons == null) {
                        maybe_cons = new Empty(menv.getPos());
                    } else {
                        maybe_cons = new ExprStmt(menv.getPos(), super_cons);
                    }
                    if (extendsClass != null) {
                        m = extendsClass.findMethod(extendsClass.getId().getName(), null);
                        if (super_cons == null && m == null) {
                            ctxt.report(new Failure(menv.getPos(),
                                                    "Constructor needs a super class constructor (maybe it's not the first statement?)."));
                        } else if (super_cons == null && extendsClass != null && m != null) {
                            SuperInvocation super_invoke = new SuperInvocation(menv.getPos(), null, null);
                            super_invoke.setFirst();
                            maybe_cons = new ExprStmt(menv.getPos(), super_invoke);
                        }
                    }
                    menv.updateBody(
                        new Block(menv.getPos(),
                    new Statement[] {
                        maybe_cons,
                        init_block,
                        menv.getBody()
                    }
                                 ));
                }
            }
            // Finally, build vtable
            vtable = new MethEnv[vfuns];
            if (vfuns > 0) {
                if (extendsClass != null) {
                    for (int i = 0; i < extendsClass.vfuns; i++) {
                        vtable[i] = extendsClass.vtable[i];
                    }
                }
                MethEnv.addToVTable(methods, vtable);
            }
            verifyInterfaces(ctxt);
        }
    }
    private void verifyInterfaces(Context ctxt)
    throws Diagnostic {
        ArrayList<Type> new_types = new ArrayList<Type>();
        for (Type t : implementsType) {
            Type checked = t.check(ctxt);
            InterfaceType iface;
            if ((iface = checked.isInterface()) == null) {
                ctxt.report(new Failure(id.getPos(),
                "Can only implement interface types. " + checked + " is not an interface."));
            } else {
                iface.checkClass(ctxt);
                for (MethEnv iface_meth : iface.getVtable()) {
                    MethEnv this_meth = findMethod(iface_meth.getName(), iface_meth.getParams());
                    MethEnv this_meth_name = findMethodByName(iface_meth.getName());
                    if (this_meth_name == null) {
                        ctxt.report(new Failure(id.getPos(),
                                                id.getName() + " does not implement method " + iface_meth.getName() + " from " +
                                                iface_meth.getOwner()));
                    } else if (this_meth == null || !this_meth.eqMethSig(true, iface_meth)) {
                        ctxt.report(new Failure(this_meth == null ? getPos() : this_meth.getPos(),
                                                id.getName() + " does not match signature of " + iface_meth.getName() + " from "
                                                + iface_meth.getOwner()));
                    }
                }
                new_types.add(checked);
            }
        }
        implementsType = new_types.toArray(new Type[0]);
    }

    /** Return the superclass, if any, of this class.
     */
    public ClassType getSuper() {
        return extendsType.isClass();
    }

    /** Test to see if this class is a supertype of another type.
     */
    public boolean isSuperOf(Type t) {
        if (t.equal(Type.NULL)) {
            return true;
        }
        ClassType that = t.isClass();
        while (that != null && that.level > level) {
            that = that.extendsType.isClass();
        }
        return (that != null && that.id.sameId(this.id));
    }

    public ArrayList<ClassType> possibleInstances() {
        ArrayList<ClassType> instancesof = new ArrayList<ClassType>();
        if (extendsType != null && extendsType.isClass() != null) {
            instancesof.addAll(extendsType.isClass().possibleInstances());
        }
        for (Type t : implementsType) {
            if (t.isClass() != null) {
                instancesof.addAll(t.isClass().possibleInstances());
            }
        }
        instancesof.add(this);
        return instancesof;
    }
    public Expression [] instancesExpr() {
        ArrayList<ClassType> instancesof = possibleInstances();
        Expression [] es = new Expression[instancesof.size()];
        int x = 0;
        for (ClassType c : instancesof) {
            es[x] = new IntLiteral(id.getPos(), c.getClassId());
            x++;
        }
        return es;
    }
    /** Add a new field to this class.
     */
    public void addField(Context ctxt, Modifiers mods, Id id, Type type,
                         Expression init_expr) {
        FieldEnv field = null;
        if (FieldEnv.find(id.getName(), fields) != null) {
            ctxt.report(new Failure(id.getPos(),
                                    "Multiple definitions for field " + id));
        } else if (mods.isStatic()) {
            field = new FieldEnv(mods, id, type, this, -1,  0, null, init_expr);
        } else {
            field = new FieldEnv(mods, id, type, this, fieldCount++, width, null,
                                 init_expr);
            width += type.size();
        }
        if (fields == null) {
            fields = field;
        } else {
            FieldEnv cur = fields;
            while (cur.getNext() != null) {
                cur = cur.getNext();
            }
            cur.setNext(field);
        }
    }

    /** Add a new method to this class.
     */
    public void addMethod(Context ctxt, Boolean is_constructor, Modifiers mods,
                          Id id, Type type,
                          VarEnv params, Statement body) {
        MethEnv found = MethEnv.find(id.getName(), params, methods);
        if (found != null) {
            ctxt.report(new Failure(id.getPos(),
                                    "Multiple definitions for method " + id));
        } else {
            int size = VarEnv.fitToFrame(params);
            int slot = (-1);
            if (!mods.isStatic()) {
                size += Assembly.WORDSIZE;      // add `this' pointer
                if (extendsType != null) {
                    // TODO: Need to check for modifiers in override
                    MethEnv env = getSuper().findMethod(id.getName(), params);
                    if (env != null && env.eqSig(false, type, params)) {
                        slot = env.getSlot();
                    } else {
                        slot = vfuns++;
                    }
                } else {
                    slot = vfuns++;
                }
            }
            methods = new MethEnv(is_constructor, mods, type, id, params, body,
                                  this, slot, size, methods);
        }
    }

    /** Look for a field by name in this class.
     */
    public FieldEnv findField(String name) {
        FieldEnv env = FieldEnv.find(name, fields);
        if (env == null && extendsType != null) {
            env = extendsType.isClass().findField(name);
        }
        return env;
    }

    /** Look for a method by name in this class.
     */
    public MethEnv findMethodByName(String name) {
        MethEnv env = MethEnv.findByName(name, methods);
        if (env == null && extendsType != null) {
            env = extendsType.isClass().findMethodByName(name);
        }
        return env;
    }

    public MethEnv findMethod(String name, VarEnv params) {
        MethEnv env = MethEnv.find(name, params, methods);
        if (env == null && extendsType != null) {
            env = extendsType.isClass().findMethod(name, params);
        }
        return env;
    }

    public MethEnv findMethodCall(String name, Context ctxt, VarEnv env,
                                  Args args) {
        VarEnv params = VarEnv.argsToVarEnv(ctxt, env, args);
        MethEnv menv = MethEnv.findCall(name, params, methods);
        if (menv == null && extendsType != null) {
            menv = extendsType.isClass().findMethodCall(name, ctxt, env, args);
        }
        return menv;
    }
    /** Check (static analysis) the definitions of the fields and
     *  methods in this class.
     */
    public void checkMembers(Context ctxt) {
        ctxt.setCurrClass(this);
        FieldEnv.checkFields(ctxt, fields);
        MethEnv.checkMethods(ctxt, methods);
        ctxt.setCurrClass(null);
    }

    /** Generate code for each of the fields and methods in this class.
     */
    public void compile(Assembly a) {
        FieldEnv.compileFields(a, fields);
        MethEnv.compileMethods(a, methods);
    }

    public ArrayList<FieldEnv> nonStaticFields() {
        ArrayList<FieldEnv> llvm_fields = new ArrayList<FieldEnv>();

        if (extendsType != null) {
            llvm_fields.addAll(((ClassType)extendsType).nonStaticFields());
        } else {
            llvm_fields = new ArrayList<FieldEnv>();
        }
        if (fields != null) {
            for (FieldEnv f : fields) {
                TypeRef t;
                if (!f.isStatic()) {
                    llvm_fields.add(f);
                }
            }
        }
        return llvm_fields;
    }

    private ArrayList llvmFields() {
        ArrayList<FieldEnv> all_fields = nonStaticFields();
        ArrayList<TypeRef> llvm_fields = new ArrayList<TypeRef>();

        /* insert vtable entry for current */
        llvm_fields.add(0, getLLVMVtable().pointerType());

        for (FieldEnv f : all_fields) {
            llvm_fields.add(f.llvmTypeField());
        }

        return llvm_fields;
    }

    public void llvmGenTypes(LLVM l) {
        if (methods != null) {
            for (MethEnv m : methods) {
                m.llvmGenTypes(l);
            }
        }
        llvmType();

        ArrayList<org.llvm.Value> vtable_inits = new ArrayList<org.llvm.Value>();
        for (MethEnv m : vtable) {
            vtable_inits.add(m.getFunctionVal(l));
        }

        llvmVtableLoc = l.getModule().addGlobal(getLLVMVtable(),
                                                id.getName() + "_vtable_loc");

        llvmVtableLoc.setInitializer(org.llvm.Value.constNamedStruct(getLLVMVtable(),
                                     vtable_inits.toArray(new org.llvm.Value[0])));

        if (fields != null) {
            for (FieldEnv f : fields) {
                if (f.isStatic()) {
                    org.llvm.Value v = l.getModule().addGlobal(f.llvmTypeField(),
                                       f.getOwner() + "_" + f.getName());
                    l.setNamedValue(f.getType().isClass() != null, f.llvmTypeField(),
                                    f.getOwner() + "_" + f.getName(), v);
                    f.setStaticField(v);

                    /* basic initialization will suffice for now */
                    v.setInitializer(f.llvmTypeField().constNull());

                }
            }
        }
    }

    public TypeRef llvmType() {
        if (llvmType == null) {
            llvmType = TypeRef.structTypeNamed(id.getName());
            TypeRef.structSetBody(llvmType, llvmFields(), false);
        }
        return llvmType;
    }

    private TypeRef getLLVMVtable() {
        if (llvmVtable == null) {
            llvmVtable = TypeRef.structTypeNamed(id.getName() + "_vtable");
            ArrayList<TypeRef> vtable_items = new ArrayList<TypeRef>();

            for (MethEnv m : vtable) {
                TypeRef t = m.llvmType().pointerType();
                vtable_items.add(t);
            }

            TypeRef.structSetBody(llvmVtable, vtable_items, false);
        }
        return llvmVtable;
    }

    public void llvmGen(LLVM l) {
        if (methods != null) {
            for (MethEnv m : methods) {
                m.llvmGenMethod(l);
            }
        }
    }
    /** Construct an object for a new object of this class.
     */
    public ObjValue newObject() {
        return new ObjValue(this, width);
    }

    /** Call the virtual method associated with a particular slot
     *  in this class.
     */
    public Value call(State st, int slot) {
        return st.call(vtable[slot]);
    }

    public org.llvm.Value defaultValue() {
        org.llvm.Value v = llvmType().pointerType().constPointerNull();
        v.setValueName("null");
        return v;
    }

    public org.llvm.Value globalInitValue(LLVM l, String name,
                                          Hashtable<String, org.llvm.Value> args) {
        ArrayList<org.llvm.Value> field_defaults = new ArrayList<org.llvm.Value>();
        args.put("classId", TypeRef.int32Type().constInt(classId, false));
        field_defaults.add(llvmVtableLoc);

        for (FieldEnv f : nonStaticFields()) {
            if (!f.isStatic()) {
                if (args.get(f.getName()) != null) {
                    field_defaults.add(args.get(f.getName()));
                } else {
                    System.out.println("WARNING: Missing Initialization for " + f.getOwner() + " " +
                                       f.getName());
                    field_defaults.add(f.getType().defaultValue());
                }
            }
        }
        return  org.llvm.Value.constNamedStruct(llvmType(),
                                                field_defaults.toArray(new org.llvm.Value[0]));
    }

    public void globalInitValue(Assembly a, String name,
                                Hashtable<String, String> args) {
        a.emitLabel(a.name(name));
        a.emit(".long", a.name(getVTName()));

        args.put("classId", Integer.toString(classId));
        for (FieldEnv f : nonStaticFields()) {
            if (!f.isStatic()) {
                if (args.get(f.getName()) != null) {
                    a.emit(".long", args.get(f.getName()));
                } else {
                    System.out.println("WARNING: Missing Initialization for " + f.getOwner() + " " +
                                       f.getName());
                    a.emit(".long", "0");
                }
            }
        }
    }

    public Expression defaultExpr(Position pos) {
        return new CastExpr(pos, this, new NullLiteral(pos));
    }
}
