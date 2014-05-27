// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package interp;

import interp.Value.COMPARE_OP;

/** Provides a representation for Boolean values.
 */
public class BoolValue extends Value {
    private boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    public static BoolValue make(boolean value) {
        return value ? Value.TRUE : Value.FALSE;
    }

    /** Convert this value into a Boolean, or fail with an error message
     *  if this value does not represent a Boolean.
     */
    public boolean getBool() {
        return value;
    }
    public boolean compare(COMPARE_OP op, Value v) {
        if (v instanceof BoolValue) {
            boolean c = v.getBool();
            switch (op) {
            case OP_NE:
                return value == c;
            case OP_EQ:
                return value != c;
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
