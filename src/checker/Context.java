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
import checker.*;

import java.util.ArrayList;
import java.util.Hashtable;

/** Provides a representation for contexts used during type checking.
 */
public final class Context extends Phase {
    private ArrayList<ClassType> classes;
    private ClassType   currClass;
    private MethEnv     currMethod;
    private int         localBytes;
    private Position pos;
    private ClassType staticClass;
    private Hashtable<String, StringLiteral> uniqueStrings;
    private int staticStringCount;
    private Id static_class_id;

    public Context(Position pos, Handler handler) {
        super(handler);
        this.pos = pos;
        this.uniqueStrings = new Hashtable<String, StringLiteral>();
        this.staticStringCount = 0;
        this.static_class_id = new Id(pos, "MJCStatic");
        this.classes = new ArrayList<ClassType>();
    }
    public void addClass(ClassType cls) {
        classes.add(cls);
    }
    public StringLiteral [] getUniqueStrings() {
        return uniqueStrings.values().toArray(new StringLiteral[0]);
    }
    public StringLiteral addStringLiteral(StringLiteral s) {
        StringLiteral result = null;
        if ((result = uniqueStrings.get(s.getString())) == null) {
            staticStringCount++;
            uniqueStrings.put(s.getString(), s);
            result = s;
        }
        return result;
    }

    public ClassType [] getClasses() {
        return classes.toArray(new ClassType[0]);
    }
    /** Look for the definition of a class by its name.
     */
    public ClassType findClass(String name) {
        for (ClassType c : classes) {
            if (name.equals(c.getId().getName())) {
                return c;
            }
        }
        return null;
    }

    /** Set the current class for this context.
     */
    public void setCurrClass(ClassType currClass) {
        this.currClass = currClass;
    }

    /** Return the current class for this context.
     */
    public ClassType getCurrClass() {
        return currClass;
    }

    /** Set the current method for this context.
     */
    public void setCurrMethod(MethEnv currMethod) {
        this.currMethod = currMethod;
        this.localBytes = 0;
    }

    /** Get the current method for this context.
     */
    public MethEnv getCurrMethod() {
        return currMethod;
    }

    /** Return boolean to indicate if this is a static context or an
     *  instance context (in which case, a this object will be present).
     */
    public boolean isStatic() {
        return currMethod == null || currMethod.isStatic();
    }

    /** Indicate whether any failures have been reported to this context.
     */
    public boolean noFailures() {
        return getHandler().getNumFailures() == 0;
    }

    /** Run static analysis checks on this context, returning a pointer
     *  to the entry point if all of the checks pass.
     */
    public MethEnv check() {
        return classes != null
               && checkClasses()
               && checkMembers() ? checkMain() : null;
    }

    /** Top-level checking of the class declarations that are included
     *  in the input program.
     */
    private boolean checkClasses() {
        for (int i = classes.size() - 1; i >= 0; i--) {
            ClassType c_i = classes.get(i);
            for (int j = 0; j < i; j++) {
                ClassType c_j = classes.get(j);
                if (c_i.getId().sameId(c_j.getId())) {
                    report(new Failure(c_i.getPos(),
                                       "Multiple definitions for class " +
                                       c_i.getId()));
                    break;
                }
            }
            try {
                c_i.checkClass(this);
            } catch (Diagnostic d) {
                report(d);
                return noFailures();
            }
        }
        for (ClassType outer_c : classes) {
            for (ClassType c : outer_c.possibleInstances()) {
                if (c.isInterface() != null) {
                    InterfaceType iface = c.isInterface();
                    iface.registerImplement(outer_c);
                }
            }
        }
        for (ClassType c : classes) {
            if (c.isInterface() != null) {
                InterfaceType iface = c.isInterface();
                iface.concreteMethods(this);
            }
        }
        if (staticClass == null) {
            Id method_id = new Id(pos, "init");
            Modifiers m = new Modifiers(pos);
            m.set(Modifiers.PUBLIC | Modifiers.STATIC);

            ArrayList<Statement> static_body = new ArrayList<Statement>();
            Decls decls = null;

            /* all static initialization of fields */
            for (ClassType c : classes) {
                ArrayList<Statement> static_init = new ArrayList<Statement>();
                if (c.getFields() != null) {
                    for (FieldEnv f : c.getFields()) {
                        Statement s = f.staticInit();
                        if (s != null) {
                            static_init.add(s);
                        }
                    }
                }
                Id id = new Id(pos, "__static_init");
                c.addMethod(this, false, m, id, Type.VOID,
                            (VarEnv)null, new Block(pos, static_init.toArray(new Statement[0])));
                static_body.add(new ExprStmt(pos, new ClassInvocation(c, id, null)));

            }
            Id names_array = new Id(pos, "names_array");
            Id instances_array = new Id(pos, "instances_array");
            Id class_ids_array = new Id(pos, "class_ids_array");
            Expression [] names = new Expression[classes.size()];
            Expression [] instances = new Expression[classes.size()];
            Expression [] class_ids = new Expression[classes.size()];
            int i = 0;
            for (ClassType c : classes) {
                names[i] = new StringLiteral(pos, c.getId().getName());
                class_ids[i] = new IntLiteral(pos, c.getClassId());
                instances[i] = new ArrayLiteral(pos, new NameType(new Name(new Id(pos,
                                                "int[]"))), c.instancesExpr());
                i++;
            }

            /* creating "Class" classes for all the classes */
            static_body.add(new LocalVarDecl(pos, findClass("int[][]"),
                                             new VarDecls(instances_array,
                                                     new ArrayLiteral(pos, findClass("int[][]"), instances))));
            static_body.add(new LocalVarDecl(pos, findClass("String[]"),
                                             new VarDecls(names_array,
                                                     new ArrayLiteral(pos, findClass("String[]"), names))));
            static_body.add(new LocalVarDecl(pos, findClass("int[]"),
                                             new VarDecls(class_ids_array,
                                                     new ArrayLiteral(pos, findClass("int[]"), class_ids))));

            static_body.add(new ExprStmt(pos,
                                         new NameInvocation(
                                             new Name(new Name(new Id(pos, "ClassPool")), new Id(pos, "addClasses")),
                                             new Args(new NameAccess(new Name(names_array)),
                                                     new Args(new NameAccess(new Name(class_ids_array)),
                                                             new Args(new NameAccess(new Name(instances_array)), null))))));
            static_body.add(new Return(pos));

            decls = new MethDecl(false, m, Type.VOID, method_id,
                                 null, new Block(pos, static_body.toArray(new Statement[0]))).link(decls);

            staticClass = new ClassType(m, static_class_id, null, new Type[0], decls);
            classes.add(staticClass);
            try {
                staticClass.checkClass(this);
            } catch (Diagnostic d) {
                report(d);
            }
        } else {
            System.out.println("Static Initialization Class Already Exists");
        }
        return noFailures();
    }

    /** Detailed static analysis for each of the members (fields and
     *  methods) of the classes in this program.
     */
    private boolean checkMembers() {
        for (ClassType c : classes) {
            setCurrClass(c);
            c.checkMembers(this);
        }
        setCurrClass(null);
        return noFailures();
    }

    /** Checks to ensure that the program has a class method "main"
     *  in a class "Main" that takes no parameters and returns no
     *  result.
     */
    private MethEnv checkMain() {
        ClassType mainClass = findClass("Main");
        if (mainClass == null) {
            report(new Failure(
                       "Program does not contain a definition for class Main"));
        } else {
            MethEnv mainMeth = mainClass.findMethod("main", null);
            if (mainMeth == null) {
                report(new Failure("No method main in class Main"));
            } else if (!mainMeth.isStatic()) {
                report(new Failure(mainMeth.getPos(),
                                   "Main.main is not static"));
            } else if (!mainMeth.eqSig(true, Type.VOID, null)) {
                report(new Failure(mainMeth.getPos(),
                                   "Main.main does not have the right type"));
            } else {
                return mainMeth;
            }
        }
        return null;
    }

    /** Return the number of bytes that are needed for local variables.
     */
    public int getLocalBytes() {
        return localBytes;
    }

    /** Reserve space in the current frame for a local variable.
     */
    public void reserveSpace(int frameOffset) {
        if (localBytes + frameOffset < 0) {
            localBytes = (-frameOffset);
        }
    }
}
