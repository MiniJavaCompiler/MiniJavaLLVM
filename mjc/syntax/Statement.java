// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for executable statements.
 */
public abstract class Statement extends Syntax {
    public Statement(Position pos) {
        super(pos);
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public abstract boolean check(Context ctxt, VarEnv env, int frameOffset);

    /** Emit code to execute this statement.
     */ 
    abstract void compile(Assembly a);

    /** Emit code that executes this statement and then branches
     *  to a specified label.
     */
    void compileThen(Assembly a, String lab) {
        compile(a);
        a.emit("jmp", lab);
    }
        
    /** Emit code that executes this statement and then returns from the
     *  current method.
     */
    public void compileRet(Assembly a) {
        compile(a);
        a.emitEpilogue();
    }

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public abstract Value exec(State st);
}
