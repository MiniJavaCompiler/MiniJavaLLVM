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
        while (formals != null) {
            Formals temp = formals.next;
            formals.next = result;
            result       = formals;
            formals      = temp;
        }
        return result;
    }
}
