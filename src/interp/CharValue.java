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

/** Provides a representation for Boolean values.
 */
public class CharValue extends Value {
    private char value;

    public CharValue(char value) {
        this.value = value;
    }

    public static CharValue make(char s) {
        return new CharValue(s);
    }
    /** Convert this value into a Boolean, or fail with an error message
     *  if this value does not represent a Boolean.
     */
    public char getChar() {
        return value;
    }
    public boolean compare(COMPARE_OP op, Value v) {
        if (v instanceof CharValue) {
            char c = v.getChar();
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
            return false;
        }
        return false;
    }
}
