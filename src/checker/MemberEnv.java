/*
 * MiniJava Compiler - X86, LLVM Compiler/Interpreter for MiniJava.
 * Copyright (C) 2014, 2008 Mitch Souders, Mark A. Smith, Mark P. Jones
 *
 * MiniJava Compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * MiniJava Compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MiniJava Compiler; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


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
