// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.Position;

/** Provides a representation for modifiers.
 */
public final class Modifiers extends Syntax {
    public Modifiers(Position pos) {
        super(pos);
    }

    private int flags = 0;

    public static final int PUBLIC    = 0x01;
    public static final int PRIVATE   = 0x02;
    public static final int PROTECTED = 0x04;
    public static final int ABSTRACT  = 0x08;
    public static final int STATIC    = 0x10;

    public boolean includes(int mask) {
        return (flags & mask) != 0;
    }

    public void set(int mask) {
        flags |= mask;
    }

    public boolean isStatic() {
        return includes(STATIC);
    }

    /** Determine whether an entity with these modifiers that was defined
     *  in the specified homeClass can be accessed from a reference that
     *  appears in code in the given fromClass.
     */
    public boolean accessible(ClassType homeClass, ClassType fromClass) {
        if (includes(PRIVATE)) {
            return homeClass == fromClass;
        } else if (includes(PROTECTED)) {
            return homeClass.isSuperOf(fromClass);
        } else {
            // By putting this case last, we make PUBLIC the default if no
            // explicit access modifier has been specified.
            return true; // You will need to modify this as part of Homework 6!
        }
    }
}
