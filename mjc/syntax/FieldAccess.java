// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.Position;

/** Provides a representation for field accesses.
 */
public abstract class FieldAccess extends LeftHandSide {
    public FieldAccess(Position pos) {
        super(pos);
    }
}
