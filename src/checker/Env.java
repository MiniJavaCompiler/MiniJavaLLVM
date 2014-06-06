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
import codegen.*;

import org.llvm.TypeRef;

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

    public org.llvm.TypeRef llvmType() {
        return type.llvmType();
    }

    public org.llvm.TypeRef llvmTypeField() {
        return type.llvmTypeField();
    }
}
