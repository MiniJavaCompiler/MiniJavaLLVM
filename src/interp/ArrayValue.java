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
import syntax.*;

/** Provides a representation for array values.
 */
public class ArrayValue extends ObjValue {
    private Value[] array;
    /** Construct an array of the specified size.
     */
    public ArrayValue(int size, ClassType type) {
        super(type, type.getWidth());
        this.array = new Value[size];
        setField(type.findField("length").getOffset(), new IntValue(size));
        setField(type.findField("array").getOffset(), this);
    }

    /** Convert this value into an ArrayValue, or fail with an error message
     *  if this value does not represent an array.
     */
    public ArrayValue getArray() {
        return this;
    }

    /** Test to see if this is a null object, in which case we report
     *  an error and abort.
     */
    private void checkIdx(int idx) {
        if (idx < 0 || idx >= this.array.length) {
            Interp.abort("Array index out of bounds!");
        }
    }

    /** Get the value stored in a particular slot of this array value.
     */
    public Value getElem(int idx) {
        checkIdx(idx);
        Value val = array[idx];
        if (val == null) {
            Interp.abort("Reading from uninitialized array slot!");
        }
        return val;
    }

    /** Set the value stored in a particular slot of this array value.
     */
    public void setElem(int idx, Value val) {
        checkIdx(idx);
        array[idx] = val;
    }
}
