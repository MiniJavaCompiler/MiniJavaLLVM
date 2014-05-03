// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package checker;

import compiler.*;
import syntax.*;

/** Provides a representation for contexts used during type checking.
 */
public final class Context extends Phase {
    private ClassType[] classes;
    private ClassType   currClass;
    private MethEnv     currMethod;
    private int         localBytes;

    public Context(Handler handler, ClassType[] classes) {
        super(handler);
        this.classes = classes;
    }

    /** Look for the definition of a class by its name.
     */
    public ClassType findClass(String name) {
        for (int i=0; i<classes.length; i++) {
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
    public MethEnv getCurrMethod () {
        return currMethod;
    }

    /** Return boolean to indicate if this is a static context or an
     *  instance context (in which case, a this object will be present).
     */
    public boolean isStatic() {
        return currMethod==null || currMethod.isStatic();
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
        return classes!=null
            && checkClasses()
            && checkMembers() ? checkMain() : null;
    }

    /** Top-level checking of the class declarations that are included
     *  in the input program.
     */
    private boolean checkClasses() {
        ClassType[] classes = this.classes;
        for (int i=classes.length-1; i>=0; i--) {
            for (int j=0; j<i; j++) {
                if (classes[i].getId().sameId(classes[j].getId())) {
                    report(new Failure(classes[i].getPos(),
                             "Multiple definitions for class " +
                             classes[i].getId()));
                    break;
                }
            }
            classes[i].checkClass(this);
        }
        return noFailures();
    }

    /** Detailed static analysis for each of the members (fields and
     *  methods) of the classes in this program.
     */
    private boolean checkMembers() {
        ClassType[] classes = this.classes;
        for (int i=0; i<classes.length; i++) {
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
        if (mainClass==null) {
            report(new Failure(
               "Program does not contain a definition for class Main"));
        } else {
            MethEnv mainMeth = mainClass.findMethod("main");
            if (mainMeth==null) {
                report(new Failure("No method main in class Main"));
            } else if (!mainMeth.isStatic()) {
                report(new Failure(mainMeth.getPos(),
                         "Main.main is not static"));
            } else if (!mainMeth.eqSig(null, null)) {
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
        if (localBytes+frameOffset < 0) {
            localBytes = (-frameOffset);
        }
    }
}
