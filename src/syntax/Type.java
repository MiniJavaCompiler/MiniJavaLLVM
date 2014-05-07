// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;

/** Provides a representation for types.
 */
public abstract class Type {
    public static Type INT     = new PrimitiveType("int");
    public static Type LONG    = new PrimitiveType("long");
    public static Type FLOAT   = new PrimitiveType("float");
    public static Type DOUBLE  = new PrimitiveType("double");
    public static Type BOOLEAN = new PrimitiveType("boolean");
    public static Type NULL    = new PrimitiveType("null");

    /** Test for equality with another type.
     */
    public abstract boolean equal(Type type);

    /** Test to see if this class is a supertype of another type.
     */
    public boolean isSuperOf(Type type) {
        return this.equal(type);
    }

    /** Test to see if this type is a class; by default, we return null.
     */
    public ClassType isClass() {
        return null;
    }

    /** Check to ensure that this is a valid type.  This is part of the
     *  mechanism used to deal with types that are specified by name,
     *  which cannot be properly resolved until parsing is complete.
     */
    public Type check(Context ctxt) {
        return this;
    }

    /** Returns the number of bytes needed to store an object of
     *  this type.
     */
    public int size() {
        return Assembly.WORDSIZE;
    }
}
