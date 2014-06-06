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
