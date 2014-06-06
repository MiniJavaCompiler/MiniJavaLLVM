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


package syntax;

import interp.*;
import util.*;
import java.lang.Iterable;
import java.util.Iterator;

/** Provides a representation for formal parameter declarations.
 */
public class Args implements Iterable<Args>, ListIteratorIF<Args> {
    private Expression arg;
    private Args next;
    public Args(Expression arg, Args next) {
        this.arg  = arg;
        this.next = next;
    }

    public Iterator<Args> iterator() {
        return new ListIterator<Args>(this);
    }

    /** Returns the expression for this argument.
     */
    public Expression getArg() {
        return arg;
    }

    public void setArg(Expression e) {
        arg = e;
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
        while (args != null) {
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
        for (; args != null; args = args.next) {
            st.push(args.arg.eval(st));
        }
    }
}
