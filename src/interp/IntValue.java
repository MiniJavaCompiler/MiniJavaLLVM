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

public class IntValue extends Value {
    private int value;

    public IntValue(int value) {
        this.value = value;
    }

    /** Convert this value into an int, or fail with an error message
     *  if this value does not represent an integer.
     */
    public int getInt() {
        return value;
    }

    public boolean compare(Value.COMPARE_OP op, Value v) {
        if (v instanceof IntValue) {
            int c = v.getInt();
            switch (op) {
            case OP_NE:
                return value != c;
            case OP_EQ:
                return value == c;
            case OP_LE:
                return value < c;
            case OP_GT:
                return value > c;
            }
        } else {
            Interp.abort("Type error: Invalid Comparison");
            return false;
        }
        return false;
    }
}
