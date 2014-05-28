// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package checker;

import compiler.*;
import syntax.*;
import checker.*;

import java.util.ArrayList;
import java.util.Hashtable;

/** Provides a representation for contexts used during type checking.
 */
public final class Context extends Phase {
    private ClassType[] classes;
    private ClassType   currClass;
    private MethEnv     currMethod;
    private int         localBytes;
    private Position pos;
    private ClassType staticClass;
    private Hashtable<String, StringLiteral> uniqueStrings;
    private StringLiteral [] strings;

    private int staticStringCount;
    private Id static_class_id;

    public Context(Position pos, Handler handler, ClassType[] classes) {
        super(handler);
        this.classes = classes;
        this.pos = pos;
        this.uniqueStrings = new Hashtable<String, StringLiteral>();
        this.staticStringCount = 0;
        this.static_class_id = new Id(pos, "MJCStatic");
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
            static_body.add(new Return(pos));

            decls = new MethDecl(false, m, Type.VOID, method_id,
                                 null, new Block(pos, static_body.toArray(new Statement[0]))).link(decls);

            staticClass = new ClassType(m, static_class_id, null, decls);
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
