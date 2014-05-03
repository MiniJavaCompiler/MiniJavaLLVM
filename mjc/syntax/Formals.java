// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

/** Provides a representation for formal parameter declarations.
 */
public class Formals {
    private Type type;
    private Id id;
    private Formals next;
    public Formals(Type type, Id id) {
        this.type = type;
        this.id   = id;
    }

    /** Returns the type for this formal parameter.
     */
    public Type getType() {
        return type;
    }

    /** Returns the identifier for this formal parameter.
     */
    public Id getId() {
        return id;
    }

    /** Returns the next formal parameter in this list.
     */
    public Formals getNext() {
        return next;
    }

    /** Link a new element onto the front of this list by setting its
     *  next pointer.
     */
    public Formals link(Formals next) {
        this.next = next;
        return this;
    }

    /** Reverse the elements in this list.
     */
    public static Formals reverse(Formals formals) {
        Formals result = null;
        while (formals!=null) {
            Formals temp = formals.next;
            formals.next = result;
            result       = formals;
            formals      = temp;
        }
        return result;
    }
}
