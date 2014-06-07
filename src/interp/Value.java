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


public abstract class Value {
    public enum COMPARE_OP {
        OP_EQ,
        OP_NE,
        OP_LE,
        OP_GT,
    };
    public static final BoolValue TRUE  = new BoolValue(true);
    public static final BoolValue FALSE = new BoolValue(false);
    public static final ObjValue  NULL  = new ObjValue(null, 0);

    /** Convert this value into an int, or fail with an error message
     *  if this value does not represent an integer.
     */
    public int getInt() {
        Interp.abort("Type error: value is not an integer");
        return 0;
    }

    public String getString() {
        Interp.abort("Type error: value is not a string");
        return "";
    }

    /** Convert this value into a Boolean, or fail with an error message
     *  if this value does not represent a Boolean.
     */
    public boolean getBool() {
        Interp.abort("Type error: value is not a Boolean");
        return false;
    }

    /** Convert this value into an ObjValue, or fail with an error message
     *  if this value does not represent an object.
     */
    public ObjValue getObj() {
        Interp.abort("Type error: value is not an object");
        return null;
    }

    /** Convert this value into an ArrayValue, or fail with an error message
     *  if this value does not represent an array.
     */
    public ArrayValue getArray() {
        Interp.abort("Type error: value is not an array");
        return null;
    }
    public char getChar() {
        Interp.abort("Type error: value is not an char");
        return 'X';
    }

    public abstract boolean compare(Value.COMPARE_OP op, Value v);
}
