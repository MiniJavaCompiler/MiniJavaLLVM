// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package checker;

import compiler.*;
import syntax.*;
import codegen.*;

/** Provides a base representation for environments.
 */
public abstract class Env {
    protected Id        id;
    protected Type      type;

    public Env(Id id, Type type) {
        this.id     = id;
        this.type   = type;
    }

    /** Returns the identifier for this entry.
     */
    public final Id getId() {
        return id;
    }

    /** Returns the name for this entry.
     */
    public final String getName() {
        return id.getName();
    }

    /** Returns the position for this entry.
     */
    public final Position getPos() {
        return id.getPos();
    }

    /** Returns the result type for this entry.
     */
    public final Type getType() {
        return type;
    }

    public org.llvm.Value llvmGen(LLVM l) {
        throw new RuntimeException(this.getClass().getName() +
                                   ": Not Yet Implemented.");
    }

    public org.llvm.Value llvmSave(LLVM l, org.llvm.Value v) {
        throw new RuntimeException(this.getClass().getName() +
                                   ": Not Yet Implemented.");
    }
}
