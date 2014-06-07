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

import syntax.*;
import checker.*;

/** Provides a representation for object values.
 */
public class ObjValue extends Value {
    private ClassType cls;
    private Value[]   fields;

    /** Construct an object of the specified class.
     */
    public ObjValue(ClassType cls, int bytes) {
        this.cls    = cls;
        this.fields = (cls == null) ? null : new Value[b2f(bytes)];
    }

    /** Convert this value into an ObjValue, or fail with an error message
     *  if this value does not represent an object.
     */
    public ObjValue getObj() {
        return this;
    }

    public ArrayValue getArray() {
        return null;
    }
    /** Describes the mapping from bytes in compiled object positions
     *  to field numbers in ObjValues.
     */
    private int b2f(int bytes) {
        return (bytes / 4) - 1;
    }

    /** Test to see if this is a null object, in which case we report
     *  an error and abort.
     */
    public void checkNull() {
        if (this.cls == null || this.fields == null) {
            Interp.abort("Null pointer error!");
        }
    }

    /** Get the value stored in a particular field of this object value.
     */
    public Value getField(int offset) {
        checkNull();
        Value val = fields[b2f(offset)];
        if (val == null) {
            Interp.abort("Attempt to use uninitialized field in "
                         + cls.getId().getName() + " (offset " + offset + ")");
        }
        return val;
    }

    /** Set the value stored in a particular field of this object value.
     */
    public void setField(int offset, Value val) {
        checkNull();
        fields[b2f(offset)] = val;
    }

    /** Call a method with a particular object and set of arguments.
     */
    public Value call(State st, int slot) {
        return cls.call(st, slot);
    }
    public boolean compare(COMPARE_OP op, Value v) {
        if (v instanceof ObjValue) {
            ObjValue c = v.getObj();
            switch (op) {
            case OP_NE:
                return this != c;
            case OP_EQ:
                return this == c;
            case OP_LE:
                Interp.abort("Type error: Invalid Comparison");
                return false;
            case OP_GT:
                Interp.abort("Type error: Invalid Comparison");
                return false;
            }
        } else {
            Interp.abort("Type error: Invalid Comparison");
            return false;
        }
        return false;
    }
}
