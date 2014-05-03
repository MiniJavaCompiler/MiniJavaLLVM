// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package checker;

import compiler.*;
import syntax.*;

/** Provides a base representation for environments.
 */
public abstract class MemberEnv extends Env {
    protected Modifiers mods;
    protected ClassType owner;

    public MemberEnv(Modifiers mods, Id id, Type type, ClassType owner) {
        super(id, type);
        this.mods  = mods;
        this.owner = owner;
    }

    /** Construct a printable description of this environment entry for
     *  use in error diagnostics.
     */
    public abstract String describe();

    /** Returns the modifiers for this entry.
     */
    public Modifiers getMods() {
        return mods;
    }

    /** Indicates whether this is a class or instance entity.
     */
    public boolean isStatic() {
      return mods.isStatic();
    }

    /** Returns the owner for this entry.
     */
    public ClassType getOwner() {
        return owner;
    }

    /** Determine whether the entity described by this environment entry
     *  can be accessed from code that appears in the given fromClass.
     */
    public void accessCheck(Context ctxt, Position pos) {
      ClassType cls = ctxt.getCurrClass();
      if (!mods.accessible(owner, cls)) {
        ctxt.report(new Failure(pos, "Cannot access " + describe()
                                   + " from the class " + cls));
      }
    }
}
