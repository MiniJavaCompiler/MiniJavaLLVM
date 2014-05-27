// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

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
