// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package checker;

import compiler.*;
import syntax.*;
import codegen.*;
import interp.*;
import util.*;
import java.lang.Iterable;
import java.util.Iterator;
/** Provides a representation for object field environments.
 */
public final class FieldEnv extends MemberEnv implements Iterable<FieldEnv>,
        ListIteratorIF<FieldEnv> {
    private FieldEnv next;
    private int      offset;  // offset of field within object (0 for static)
    public FieldEnv(Modifiers mods, Id id, Type type, ClassType owner,
                    int offset, FieldEnv next) {
        super(mods, id, type, owner);
        this.offset = offset;
        this.next   = next;
    }
    public Iterator<FieldEnv> iterator() {
        return new ListIterator<FieldEnv>(this);
    }
    public int getOffset() {
        return offset;
    }

    public FieldEnv getNext() {
        return next;
    }
    /** Look for the entry corresponding to a particular identifier
     *  in a given environment.
     */
    public static FieldEnv find(String name, FieldEnv env) {
        while (env != null && !name.equals(env.id.getName())) {
            env = env.next;
        }
        return env;
    }

    /** Check a list of field definitions.
     */
    public static void checkFields(Context ctxt, FieldEnv env) {
        // No action required here!
    }

    /** Construct a printable description of this environment entry for
     *  use in error diagnostics.
     */
    public String describe() {
        return "field " + id;
    }

    /** Compile a list of (global) variable definitions.
     */
    public static void compileFields(Assembly a, FieldEnv env) {
        for (; env != null; env = env.next) {
            env.compileField(a);
        }
    }

    /** Output the code to reserve space for a declared field.  Only the
     *  static fields need space reserved in this way; space for non-static
     *  fields is allocated dynamically as part of object creation.
     */
    void compileField(Assembly a) {
        if (isStatic()) {
            a.emitVar(this, type.size());
        }
    }

    public void llvmGen(LLVM l) {
        if (isStatic()) {
            throw new RuntimeException("Does not currently support static variables");
        }
    }

    /** Generate code to load the value of a field from an object whose
     *  address is passed in through the current free register.
     */
    public void loadField(Assembly a, int free) {
        if (isStatic()) {
            a.emit("movl", staticName(a), a.reg(free));
        } else {
            a.emit("movl", a.indirect(offset, a.reg(free)), a.reg(free));
        }
    }

    /** Save the value in the free register in a field of the object that
     *  is pointed to by register (free+1).
     */
    public void saveField(Assembly a, int free) {
        if (isStatic()) {
            a.emit("movl", a.reg(free), staticName(a));
        } else {
            a.emit("movl", a.reg(free), a.indirect(offset, a.reg(free + 1)));
        }
    }

    /** Mangle the class and field name to make a label for a global
     *  (static) field.
     */
    public String staticName(Assembly a) {
        return a.mangle("v_" + owner.toString(), getName());
    }

    // For interpreter use only: -------------------
    private Value val = null;  // used for static fields

    public Value getField(ObjValue obj) {
        if (isStatic()) {
            if (val == null) {
                Interp.abort("Attempt to use uninitialized static variable!");
            }
            return val;
        } else {
            return obj.getField(offset);
        }
    }

    public void setField(ObjValue obj, Value val) {
        if (isStatic()) {
            this.val = val;
        } else {
            obj.setField(offset, val);
        }
    }

}
