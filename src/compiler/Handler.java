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

/** Represents a handler for diagnostics.  In particular applications,
 *  we can use subclasses to specify how diagnostics should be handled.
 */
public abstract class Handler {
    /** Count how many diagnostics have been reported.
     */
    private int numDiagnostics = 0;
    public int getNumDiagnostics() {
        return numDiagnostics;
    }

    /** Count how many failures have been reported.
     */
    private int numFailures = 0;
    public int getNumFailures() {
        return numFailures;
    }

    /** Report a problem to this diagnostic handler.
     */
    public void report(Diagnostic d) {
        numDiagnostics++;
        if (d instanceof Failure) {
            numFailures++;
        }
        respondTo(d);
    }

    /** Respond to a diagnostic report.  Subclasses should
     *  override this method to deal with diagnostic reports in an
     *  appropriate way.  Diagnostics will normally be passed to this
     *  method indirectly via a call to report() in the client code.
     */
    protected abstract void respondTo(Diagnostic d);

    /** Reset the diagnostic handler.  This should set the diagnostic
     *  handler back to the state of a freshly created handler.  As a
     *  default, we just reset the counters.
     */
    public void reset() {
        numDiagnostics = 0;
        numFailures    = 0;
    }
}

