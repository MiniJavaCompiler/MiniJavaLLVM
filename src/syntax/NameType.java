// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;
import codegen.*;

import org.llvm.TypeRef;

/** Provides a representation for types that have been mentioned by
 *  name but not yet verified.
 */
public final class NameType extends Type {
    private Name name;
    public NameType(Name name) {
        this.name = name;
    }

    /** Return a printable representation of this type.
     */
    public String toString() {
        return name.toString();
    }

    public TypeRef llvmType() {
        throw new RuntimeException("Type has not been determined.");
    }
    public org.llvm.Value defaultValue() {
        throw new RuntimeException("Type has not been determined.");
    }
    public void llvmGenTypes(LLVM l) {
        throw new RuntimeException("Type has not been determined.");
    }
    /** Check to ensure that this is a valid type.  Used to deal with
     *  types that are specified by name, which cannot be properly
     *  resolved until parsing is complete.
     */
    public Type check(Context ctxt) {
        ClassType cls = name.asClass(ctxt);
        if (cls == null) {
            ctxt.report(new Failure(name.getPos(), "Unknown type " + name));
        }
        return cls;
    }

    /** Test for equality with another type.
     */
    public boolean equal(Type type) {
        // This method should never be called; it might be better
        // to register some kind of internal error at this point.
        return false;
    }

    public int getWidth() {
        return Assembly.WORDSIZE;
    }
}
