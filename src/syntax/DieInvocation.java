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
public class DieInvocation extends ExternalInvocation {
    public DieInvocation(Position pos) {
        super(
            new Name(new Name(new Id(pos, "MJC")), new Id(pos, "die")), null);
    }
    public Value eval(State st) {
        System.exit(-1);
        return null;
    }
}
