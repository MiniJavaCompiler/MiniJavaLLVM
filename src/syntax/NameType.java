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
    public Expression defaultExpr(Position pos) {
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
