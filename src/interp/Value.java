// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package interp;

public abstract class Value {
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
}
