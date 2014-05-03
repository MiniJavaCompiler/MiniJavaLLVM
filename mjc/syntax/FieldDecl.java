// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import checker.*;

/** Provides a representation for a field declaration in a class.
 */
public class FieldDecl extends Decls {
    private Type     type;
    private VarDecls vardecls;

    public FieldDecl(Modifiers mods, Type type, VarDecls vardecls) {
        super(mods);
        this.type     = type;
        this.vardecls = vardecls;
    }

    /** Add a declared item to a specified class.
     */
    public void addToClass(Context ctxt, ClassType cls) {
        type = type.check(ctxt);
        if (type != null) {
            for (VarDecls vs = vardecls; vs != null; vs = vs.getNext()) {
                cls.addField(ctxt, mods, vs.getId(), type);
            }
        }
    }
}
