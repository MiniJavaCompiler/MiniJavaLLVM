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

import compiler.*;

/** Provides a representation for identifiers annotated with positions.
 */
public final class Id extends Syntax {
    private String name;
    public Id(Position pos, String name) {
        super(pos);
        this.name = name;
    }

    /** Returns the name for this identifier.
     */
    public String getName() {
        return name;
    }

    /** Determine whether two identifiers have the same name, ignoring
     *  position details.
     */
    public boolean sameId(Id id) {
        return name.equals(id.name);
    }

    /** Return a string representation for this identifier.
     */
    public String toString() {
        return name;
    }
}
