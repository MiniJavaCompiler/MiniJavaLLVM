// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

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
}
