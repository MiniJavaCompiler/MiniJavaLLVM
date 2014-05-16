// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

/** Provides a representation for variable declaration lists.
 */
public class VarDecls {
    private Id id;
    private VarDecls next;
    private Expression init;
    public VarDecls(Id id) {
        this.id   = id;
    }

    public VarDecls(Id id, Expression init) {
        this.id = id;
        this.init = init;
    }

    public Expression getInitExpr() {
        return this.init;
    }

    /** Returns the identifier for this variable declaration.
     */
    public Id getId() {
        return id;
    }

    /** Returns the next argument in this list.
     */
    public VarDecls getNext() {
        return next;
    }

    /** Link a new element onto the front of this list by setting its
     *  next pointer.
     */
    public VarDecls link(VarDecls next) {
        this.next = next;
        return this;
    }

    /** Reverse the elements in this list.
     */
    public static VarDecls reverse(VarDecls vardecls) {
        VarDecls result = null;
        while (vardecls != null) {
            VarDecls temp = vardecls.next;
            vardecls.next = result;
            result        = vardecls;
            vardecls      = temp;
        }
        return result;
    }
}
