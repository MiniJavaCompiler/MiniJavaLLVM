/*
 * MiniJava Compiler - X86, LLVM Compiler/Interpreter for MiniJava.
 * Copyright (C) 2014, 2008 Mitch Souders, Mark A. Smith, Mark P. Jones
 *
 * MiniJava Compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * MiniJava Compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MiniJava Compiler; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


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
