// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;
import syntax.*;

import java.util.ArrayList;
import org.llvm.TypeRef;

/** Provides a representation for method invocations.
 */
public class AllocArrayInvocation extends ExternalInvocation {
    private ArrayType object;
    private Expression size;
    public AllocArrayInvocation(Position pos, ArrayType t, Expression size) {
        super(
            new Name(new Name(new Id(pos, "MJC")), new Id(pos, "allocArray")),
            new Args(size,
                     new Args(new TypeLen(pos, t.getElementType()), null)));
        this.object = t;
        this.size = size;
    }
    public Value eval(State st) {
        return new ArrayValue(size.eval(st).getInt(), object);
    }
}
