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
    private ArrayType arrayType;
    private Type array;
    private Type elem;
    private Expression size;
    public AllocArrayInvocation(Position pos, Type array, Type elem,
                                Expression size) {
        super(
            new Name(new Name(new Id(pos, "MJC")), new Id(pos, "allocArray")),
            new Args(size,
                     new Args(new TypeLen(pos, elem), null)));
        this.array = array;
        this.elem = elem;
        this.size = size;
    }
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        arrayType = array.check(ctxt).isArray();
        if (arrayType == null) {
            throw new Failure("AllocArrayInvocation expects array type");
        }
        return super.typeOf(ctxt, env);
    }
    public Value eval(State st) {
        return new ArrayValue(size.eval(st).getInt(), arrayType);
    }
}
