// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package interp;

/** Provides a representation for Boolean values.
 */
public class StringValue extends Value {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public static StringValue make(String s) {
        return new StringValue(s);
    }
    /** Convert this value into a Boolean, or fail with an error message
     *  if this value does not represent a Boolean.
     */
    public String getString() {
        return value;
    }
}
