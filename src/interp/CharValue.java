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
}
