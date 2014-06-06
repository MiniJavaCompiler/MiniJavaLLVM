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

/** Base class for compiler phases.  Its only real purpose is to provide
 *  convenient access to a diagnostic handler.
 */
public abstract class Phase {
    private Handler handler;

    /** Construct a new phase with a specified diagnostic handler.
     */
    protected Phase(Handler handler) {
        this.handler = handler;
    }

    /** Return the handler for this phase.
     */
    public Handler getHandler() {
        return handler;
    }

    /** Report a diagnostic detected in this phase.
     */
    public void report(Diagnostic d) {
        if (handler != null) {
            handler.report(d);
        }
    }
}
