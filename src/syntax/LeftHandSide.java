// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import codegen.*;
import interp.*;

/** Provides a representation for left hand sides of assignments.
 */
public abstract class LeftHandSide extends Expression {
    public LeftHandSide(Position pos) {
        super(pos);
    }

    /** Save the value in the free register in the variable specified by
     *  this expression.
     */
    abstract void saveVar(Assembly a, int free);

    /** Save a value in the location specified by this left hand side.
     */
    public abstract void save(State st, Value val);
}
