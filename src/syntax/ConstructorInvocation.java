// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

import org.llvm.Builder;

/** Represents an instance method invocation.
 */
public final class ConstructorInvocation extends ObjectInvocation {
    public ConstructorInvocation(Name n, Args args) {
        super(
            new NewExpr(n.getPos(), n), n.getId(), args);
    }
}
