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

/** A base class for objects that represent compiler diagnostics.
 *  Errors should be implemented as subclasses of Failure, while
 *  warnings are normally implemented as as subclasses of Warning.
 */
public abstract class Diagnostic extends Exception {
    /** Used to hold a simple description of the problem that
     *  occurred.  This field is used only by the default
     *  implementation of getDescription() that is provided in
     *  this class.  More complex diagnostics are likely to
     *  override this method, and hence will not use this field.
     *
     *  The format and interpretation of the description field
     *  have not yet been determined.  It would, however, make
     *  sense to allow the use of some kind of XML/HTML tags to
     *  allow embedding of structure/formatting hints.
     */
    private String text;
    public String getText() {
        return text;
    }

    /** A pointer to the place where the error was detected.
     *  A null value can be used for diagnostics that are not
     *  associated with any particular point in the source.
     */
    private Position position;
    public Position getPos() {
        return position;
    }

    /** Return a cross reference string for this diagnostic.  The
     *  format and interpretation of this string has not yet
     *  determined, but might, for example, be used to construct
     *  a URL or file name that contains additional information
     *  about problems of this kind.
     */
    protected String crossRef;
    public String getCrossRef() {
        return null;
    }

    /** Construct a simple diagnostic with a fixed description.
     */
    public Diagnostic(String text) {
        this.text = text;
    }

    /** Construct a diagnostic object for a particular source position.
     */
    public Diagnostic(Position position) {
        this.position = position;
    }

    /** Construct a simple diagnostic with a fixed description
     *  and a source position.
     */
    public Diagnostic(Position position, String text) {
        this.position = position;
        this.text     = text;
    }
}
