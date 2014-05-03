// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package interp;

/** Provides a representation for array values.
 */
class ArrayValue {
    private Value[] array;

    /** Construct an array of the specified size.
     */
    public ArrayValue(int size) {
      this.array = new Value[size];
    }

    /** Convert this value into an ArrayValue, or fail with an error message
     *  if this value does not represent an array.
     */  
    public ArrayValue getArray() {
        return this;
    } 

    /** Test to see if this is a null object, in which case we report
     *  an error and abort.
     */
    private void checkIdx(int idx) {
        if (idx<0 || idx>=this.array.length) {
            Interp.abort("Array index out of bounds!");
        }
    }

    /** Get the value stored in a particular slot of this array value.
     */
    public Value getElem(int idx) {
        checkIdx(idx);
        Value val = array[idx];
        if (val==null) {
            Interp.abort("Reading from uninitialized array slot!");
        }
        return val;
    }

    /** Set the value stored in a particular slot of this array value.
     */
    public void setElem(int idx, Value val) {
        checkIdx(idx);
        array[idx] = val;
    }
}
