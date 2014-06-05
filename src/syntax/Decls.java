// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import checker.Context;

/** Provides a representation for lists of declarations.
 */
public abstract class Decls {
    protected Modifiers mods;
    private   Decls     next;

    public Modifiers getMods() {
        return mods;
    }
    public Decls(Modifiers mods) {
        this.mods = mods;
    }

    /** Returns the next argument in this list.
     */
    public Decls getNext() {
        return next;
    }

    /** Link a new element onto the front of this list by setting its
     *  next pointer.
     */
    public Decls link(Decls next) {
        this.next = next;
        return this;
    }

    /** Reverse the elements in this list.
     */
    public static Decls reverse(Decls decls) {
        Decls result = null;
        while (decls != null) {
            Decls temp = decls.next;
            decls.next = result;
            result     = decls;
            decls      = temp;
        }
        return result;
    }

    /** Add a declared item to a specified class.
     */
    public abstract void addToClass(Context ctxt, ClassType cls);
}
