// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package interp;

import checker.*;

/** Represents the interpreter stack.
 */
public class State {
    private Value[] stack;
    private int     sp;
    private int     bp;

    public State() {
        stack = new Value[1000];
        sp    = stack.length;
        bp    = sp;
    }

    /** Push a value on to the stack.
     */
    public void push(Value v) {
        if (--sp < 0) {
            Interp.abort("Stack overflow!");
        }
        stack[sp] = v;
    }

    /** Return the "this" object for the current stack frame,
     *  given its offset in the stack frame.
     */
    public ObjValue getThis(int size) {
        return getFrame(size + 4).getObj();
    }

    /** Describes the mapping from byte offsets in the compiler stack
     *  frame to absolute positions in the stack.
     */
    private int b2p(int bytes) {
        int pos = bp + (bytes / 4);
        // the interpreter doesn't store the return address of a method
        // or the old base pointer in the stack frame, so we can shift
        // the offset for parameters down by two stack slots.
        return (bytes > 0) ? (pos - 2) : pos;
    }

    /** Return the value held at a particular offset in the current
     *  stack frame.
     */
    public Value getFrame(int offset) {
        Value val = stack[b2p(offset)];
        if (val == null) {
            Interp.abort("Attempt to use uninitialized variable from the stack!");
        }
        return val;
    }

    /** Set the value held at a particular offset in the current
     *  stack frame.
     */
    public void setFrame(int offset, Value val) {
        stack[b2p(offset)] = val;
    }

    /** Call a method, assuming that the (optional) this parameter
     *  and any additional arguments have already been pushed onto
     *  the stack.
     */
    public Value call(MethEnv menv) {
        int savedBP = bp;
        bp          = sp;
        sp         -= (menv.getLocals() / 4);
        Value rv    = menv.run(this);
        sp          = bp + (menv.getSize() / 4);
        bp          = savedBP;
        return rv;
    }
}
