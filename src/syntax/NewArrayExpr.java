// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;
import org.llvm.Builder;
import org.llvm.TypeRef;

/** Provides a representation for "new" expressions that allocate an
 *  instance of a class.
 */
public final class NewArrayExpr extends NewExpr {
    private Type elementType;
    private ClassType  cls;
    private Expression size;
    private Name name;
    public static Name buildArrayExprName(Position pos, Type elementType) {
        return new Name(new Id(pos, elementType.toString() + "[]"));
    }

    public NewArrayExpr(Position pos, Type elementType, Expression size) {
        super(pos, buildArrayExprName(pos, elementType));
        this.size = size;
        this.cls = cls;
        this.name = buildArrayExprName(pos, elementType);
        this.elementType = elementType;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        if (size.typeOf(ctxt, env) != Type.INT) {
            throw new Failure(pos, "Array size must be of Type INT");
        }
        return super.typeOf(ctxt, env);
    }
}
