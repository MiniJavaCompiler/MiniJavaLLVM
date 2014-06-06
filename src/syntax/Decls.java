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
