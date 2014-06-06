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

import compiler.Position;

/** Provides a representation for modifiers.
 */
public final class Modifiers extends Syntax {
    public Modifiers(Position pos) {
        super(pos);
    }

    private int flags = 0;

    public static final int PUBLIC    = 0x01;
    public static final int PRIVATE   = 0x02;
    public static final int PROTECTED = 0x04;
    public static final int ABSTRACT  = 0x08;
    public static final int STATIC    = 0x10;

    public boolean includes(int mask) {
        return (flags & mask) != 0;
    }

    public void set(int mask) {
        flags |= mask;
    }

    public boolean isStatic() {
        return includes(STATIC);
    }

    /** Determine whether an entity with these modifiers that was defined
     *  in the specified homeClass can be accessed from a reference that
     *  appears in code in the given fromClass.
     */
    public boolean accessible(ClassType homeClass, ClassType fromClass) {
        if (fromClass.getId().getName().equals("MJCStatic")) {
            /* special case to allow static field initialization from a global
               static class */
            return true;
        } else if (includes(PRIVATE)) {
            return homeClass == fromClass;
        } else if (includes(PROTECTED)) {
            return homeClass.isSuperOf(fromClass);
        } else {
            // By putting this case last, we make PUBLIC the default if no
            // explicit access modifier has been specified.
            return true; // You will need to modify this as part of Homework 6!
        }
    }
}
