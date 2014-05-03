// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for "new" expressions that allocate an
 *  instance of a class.
 */
public final class NewExpr extends StatementExpr {
    private Name      name;
  //private Args      args;
    private ClassType cls;

    public NewExpr(Position pos, Name name /*, Args args */) {
        super(pos);
        this.name = name;
      //this.args = args;
        this.cls  = null;
    }

    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env) throws Diagnostic {
        cls = name.asClass(ctxt);
        if (cls==null) {
            throw new Failure(pos, "Undefined name " + name);
        } 
        return cls;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        a.spillAll(free);
        a.emit("pushl", a.vtAddr(cls));
        a.call(a.name("new_object"), free, a.WORDSIZE);
        a.unspillAll(free);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        return cls.newObject();
    }
}
