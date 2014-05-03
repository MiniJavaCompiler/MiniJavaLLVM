// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

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
