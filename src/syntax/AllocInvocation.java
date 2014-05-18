// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

import java.util.ArrayList;
import org.llvm.TypeRef;

/** Provides a representation for method invocations.
 */
public class AllocInvocation extends ExternalInvocation {
    private ClassType object;
    public AllocInvocation(Position pos, ClassType t) {
        super(
            new Name(new Name(new Id(pos, "MJC")), new Id(pos, "allocObject")),
            new Args(new TypeLen(pos, t), null));
        this.object = t;
    }
    public Value eval(State st) {
        return object.newObject();
    }
}
