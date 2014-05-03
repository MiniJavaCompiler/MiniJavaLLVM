// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import interp.*;

/** Provides a representation for formal parameter declarations.
 */
public class Args {
    private Expression arg;
    private Args next;
    public Args(Expression arg, Args next) {
        this.arg  = arg;
        this.next = next;
    }

    /** Returns the expression for this argument.
     */
    public Expression getArg() {
        return arg;
    }

    /** Returns the next argument in this list.
     */
    public Args getNext() {
        return next;
    }

    /** Link a new element onto the front of this list by setting its
     *  next pointer.
     */
    public Args link(Args next) {
        this.next = next;
        return this;
    }

    /** Reverse the elements in this list.
     */
    public static Args reverse(Args args) {
        Args result = null;
        while (args!=null) {
            Args temp = args.next;
            args.next = result;
            result    = args;
            args      = temp;
        }
        return result;
    }

    /** Evaluate the arguments in this list and push them on to the stack.
     */
    public static void push(State st, Args args) {
        for (; args!=null; args=args.next) {
            st.push(args.arg.eval(st));
        }
    }
}
