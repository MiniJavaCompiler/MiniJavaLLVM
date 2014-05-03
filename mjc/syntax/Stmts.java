// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import checker.*;
import codegen.*;
import interp.*;

/** Provides a representation for the statements in a block.
 */
public abstract class Stmts {
    protected Stmts next;
    public Stmts(Stmts next) {
        this.next = next;
    }

    /** Reverse the elements in this list.
     */
    public static Stmts reverse(Stmts stmts) {
        Stmts result = null;
        while (stmts!=null) {
            Stmts tmp  = stmts.next;
            stmts.next = result;
            result     = stmts;
            stmts      = tmp;
        }
        return result;
    }

    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public abstract boolean check(Context ctxt, VarEnv env, int frameOffset);

    /** Emit code to execute this statement.
     */ 
    abstract void compile(Assembly a);
     
    /** Emit code that executes these statements and then branches
     *  to a specified label.
     */
    abstract void compileThen(Assembly a, String lab);
    
    /** Emit code that executes these statements and then returns from the
     *  current method.
     */
    abstract void compileRet(Assembly a);

    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public abstract Value exec(State st);
}
