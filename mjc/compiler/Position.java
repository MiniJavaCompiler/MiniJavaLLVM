// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package compiler;

/** An abstraction of a position within a source document.
 *  Source code may come from many different sources: a text file,
 *  a compressed file, a web page, a string, a database, a data structure
 *  in memory, a console input stream, a network resource, etc.  In each
 *  case we need to be able to give a name to source code positions so
 *  that we can tell users where certain values are defined, or where
 *  errors have been detected.
 * 
 *  [We might also like to have a mechanism for starting an editor at a
 *  particular position so that users can view and potentially change the
 *  corresponding sections of source code.]
 */
public abstract class Position {
    /** Obtain a printable description of the source position.
     */
    public abstract String describe();

    /** Return a column number for this position.  By convention,
     *  column numbers start at zero.  If no sensible column number
     *  value is available, then we return zero.  Calling methods
     *  will not be able to distinguish between uses of zero as a
     *  genuine column number, and uses of zero as a "no column number
     *  available" indicator.
     */
    public int getColumn() {
        return 0;
    }

    /** Return a row number for this position.  By convention,
     *  row numbers start at zero.  If no sensible row number
     *  value is available, then we return zero.  Calling methods
     *  will not be able to distinguish between uses of zero as a
     *  genuine row number, and uses of zero as a "no row number
     *  available" indicator.
     */
    public int getRow() {
        return 0;
    }

    /** Copy a source position.
     */
    public abstract Position copy();
}
