// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package interp;

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
}
