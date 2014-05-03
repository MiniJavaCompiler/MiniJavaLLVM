// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;

/** Provides a representation for literal expressions.
 */
public abstract class Literal extends Expression {
    public Literal(Position pos) {
        super(pos);
    }

    /** Return the depth of this expression tree as a measure of its
     *  complexity.  Literals never require any evaluation so they
     *  always have zero depth.
     */ 
    int getDepth() {
        return 0;
    }  
}
