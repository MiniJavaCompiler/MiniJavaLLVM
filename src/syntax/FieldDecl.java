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
                cls.addField(ctxt, mods, vs.getId(), type, vs.getInitExpr());
            }
        }
    }
}
