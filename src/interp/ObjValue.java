// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package interp;

import syntax.*;
import checker.*;

/** Provides a representation for object values.
 */
public class ObjValue extends Value {
    private ClassType cls;
    private Value[]   fields;

    /** Construct an object of the specified class.
     */
    public ObjValue(ClassType cls, int bytes) {
        this.cls    = cls;
        this.fields = (cls == null) ? null : new Value[b2f(bytes)];
    }

    /** Convert this value into an ObjValue, or fail with an error message
     *  if this value does not represent an object.
     */
    public ObjValue getObj() {
        return this;
    }

    public ArrayValue getArray() {
        return null;
    }
    /** Describes the mapping from bytes in compiled object positions
     *  to field numbers in ObjValues.
     */
    private int b2f(int bytes) {
        return (bytes / 4) - 1;
    }

    /** Test to see if this is a null object, in which case we report
     *  an error and abort.
     */
    public void checkNull() {
        if (this.cls == null || this.fields == null) {
            Interp.abort("Null pointer error!");
        }
    }

    /** Get the value stored in a particular field of this object value.
     */
    public Value getField(int offset) {
        checkNull();
        Value val = fields[b2f(offset)];
        if (val == null) {
            Interp.abort("Attempt to use uninitialized field in "
                         + cls.getId().getName() + " (offset " + offset + ")");
        }
        return val;
    }

    /** Set the value stored in a particular field of this object value.
     */
    public void setField(int offset, Value val) {
        checkNull();
        fields[b2f(offset)] = val;
    }

    /** Call a method with a particular object and set of arguments.
     */
    public Value call(State st, int slot) {
        return cls.call(st, slot);
    }
    public boolean compare(COMPARE_OP op, Value v) {
        if (v instanceof ObjValue) {
            ObjValue c = v.getObj();
            switch (op) {
            case OP_NE:
                return this != c;
            case OP_EQ:
                return this == c;
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
