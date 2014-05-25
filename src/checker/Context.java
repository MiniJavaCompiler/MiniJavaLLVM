// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package checker;

import compiler.*;
import syntax.*;
import checker.*;

import java.util.ArrayList;

/** Provides a representation for contexts used during type checking.
 */
public final class Context extends Phase {
    private ClassType[] classes;
    private ClassType   currClass;
    private MethEnv     currMethod;
    private int         localBytes;
    private Position pos;
    private ClassType staticClass;
    private StringLiteral [] strings;

    public Context(Position pos, Handler handler, ClassType[] classes,
                   StringLiteral [] strings) {
        super(handler);
        this.classes = classes;
        this.pos = pos;
        this.strings = strings;
    }

    public ClassType [] getClasses() {
        return classes;
    }
    /** Look for the definition of a class by its name.
     */
    public ClassType findClass(String name) {
        for (int i = 0; i < classes.length; i++) {
            if (name.equals(classes[i].getId().getName())) {
                return classes[i];
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
        ClassType[] classes = this.classes;
        for (int i = classes.length - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (classes[i].getId().sameId(classes[j].getId())) {
                    report(new Failure(classes[i].getPos(),
                                       "Multiple definitions for class " +
                                       classes[i].getId()));
                    break;
                }
            }
            classes[i].checkClass(this);
        }

        if (staticClass == null) {
            Id method_id = new Id(pos, "init");
            Id class_id = new Id(pos, "MJCStatic");
            Modifiers m = new Modifiers(pos);
            m.set(Modifiers.PUBLIC | Modifiers.STATIC);

            ArrayList<Statement> static_body = new ArrayList<Statement>();
            Decls decls = null;

            for (int i = classes.length - 1; i >= 0; i--) {
                if (classes[i].getFields() != null) {
                    for (FieldEnv f : classes[i].getFields()) {
                        Statement s = f.staticInit();
                        if (s != null) {
                            static_body.add(s);
                        }
                    }
                }
            }

            Type char_arr_class = findClass("char[]");
            Type string_class = findClass("String");
            Id tmp_char = new Id(pos, "tmp_char");
            static_body.add(new LocalVarDecl(pos, char_arr_class, new VarDecls(tmp_char,
                                             null)));
            int global_string_index = 0;

            for (StringLiteral s : strings) {
                Id str_id = new Id(pos, "global_string" +  global_string_index++);
                s.setName(class_id, str_id);
                decls = new FieldDecl(m, string_class, new VarDecls(str_id,
                                      (Expression)null)).link(decls);
                static_body.add(
                    new ExprStmt(pos,
                                 new AssignExpr(pos, new NameAccess(new Name(tmp_char)),
                                                new ConstructorInvocation(new Name(new Id(pos, "char[]")),
                                                        new Args(new IntLiteral(pos, s.getString().length()), null)))));
                for (int x = 0; x < s.getString().length(); x++) {
                    static_body.add(
                        new ExprStmt(pos,
                                     new AssignExpr(pos,
                                                    new ArrayAccess(pos, new NameAccess(new Name(tmp_char)), new IntLiteral(pos,
                                                            x)),
                                                    new CharLiteral(pos, s.getString().charAt(x)))));
                }
                static_body.add(new ExprStmt(pos,
                                             new AssignExpr(pos, new NameAccess(s.getName()),
                                                     new NameInvocation(new Name(new Name(new Id(pos, "String")), new Id(pos,
                                                             "makeStringChar")),
                                                             new Args(new NameAccess(new Name(tmp_char)), null)))));
            }

            decls = new MethDecl(false, m, Type.VOID, method_id,
                                 null, new Block(pos, static_body.toArray(new Statement[0]))).link(decls);

            staticClass = new ClassType(m, class_id, null, decls);
            ClassType [] new_classes = new ClassType[classes.length + 1];
            int x = 0;
            for (ClassType c : classes) {
                new_classes[x] = c;
                x++;
            }
            new_classes[classes.length] = staticClass;
            this.classes = new_classes;
            staticClass.checkClass(this);
        } else {
            System.out.println("Static Initialization Class Already Exists");
        }
        return noFailures();
    }

    /** Detailed static analysis for each of the members (fields and
     *  methods) of the classes in this program.
     */
    private boolean checkMembers() {
        ClassType[] classes = this.classes;
        for (int i = 0; i < classes.length; i++) {
            setCurrClass(classes[i]);
            classes[i].checkMembers(this);
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
            MethEnv mainMeth = mainClass.findMethod("main");
            if (mainMeth == null) {
                report(new Failure("No method main in class Main"));
            } else if (!mainMeth.isStatic()) {
                report(new Failure(mainMeth.getPos(),
                                   "Main.main is not static"));
            } else if (!mainMeth.eqSig(Type.VOID, null)) {
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
