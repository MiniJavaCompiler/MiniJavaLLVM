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


package compiler;

/** A simple implementation of the Handler interface that prints the
 *  position and description of each diagnostic on System.err, and
 *  then returns to the caller.
 */
public class SimpleHandler extends Handler {
    /** Respond to a diagnostic by displaying it on the error output
     *  stream.
     */
    protected void respondTo(Diagnostic d) {
        Position pos  = d.getPos();
        if (d instanceof Warning) {
            System.err.print("WARNING: ");
        } else {
            System.err.print("ERROR: ");
        }
        if (pos != null) {
            System.err.println(pos.describe());
        }
        String txt = d.getText();
        if (txt != null) {
            System.err.println(txt);
        }
        System.err.println();
    }
}
