// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;

/** Provides a representation for syntactic elements, each of which is
 *  annotated with a position in the input file.
 */
public abstract class Syntax {
    protected Position pos;

    public Syntax(Position pos) {
        this.pos = pos;
    }

    /** Returns the position of this syntactic element.
     */
    public Position getPos() {
        return pos;
    }
}
