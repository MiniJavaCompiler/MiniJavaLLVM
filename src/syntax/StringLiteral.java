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
import interp.*;

import org.llvm.TypeRef;

import java.util.Hashtable;
/** Provides a representation for String literals.
 */
public final class StringLiteral extends Literal {
    private String value;
    private org.llvm.Value actual;
    ClassType stringType = null;
    ArrayType charArrType = null;
    StringLiteral master;
    String x86name;
    ObjValue interp_obj = null;

    public StringLiteral(Position pos, String literal) {
        super(pos);
        this.value = literal;
        this.actual = null;
        this.stringType = null;
        this.master = null;
        this.x86name = null;
    }
    public String getString() {
        return this.value;
    }
    /** Check this expression and return an object that describes its
     *  type (or throw an exception if an unrecoverable error occurs).
     */
    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        stringType = ctxt.findClass("String");
        charArrType = ctxt.findClass("char[]").isArray();
        this.master = ctxt.addStringLiteral(this);
        return stringType;
    }

    /** Generate code to evaluate this expression and
     *  leave the result in the specified free variable.
     */
    public void compileExpr(Assembly a, int free) {
        if (this != master) {
            master.compileExpr(a, free);
        } else {
            a.emit("movl", '$' + a.name(x86name),  a.reg(free));
        }
    }

    public void emitStaticString(Assembly a, int n) {
        String [] elements = new String[value.length()];
        x86name = "str_" + n + "_lit";
        for (int x = 0; x < value.length(); x++) {
            elements[x] = Integer.toString((int)value.charAt(x));
        }
        String char_name = a.name("char_" + n + "_lit");
        charArrType.globalInitValue(a, char_name, elements);

        Hashtable<String, String> args = new Hashtable<String, String>();
        args.put("string", char_name);

        stringType.globalInitValue(a, x86name, args);
    }

    public void emitStaticString(LLVM l) {
        String name = "static_string";
        org.llvm.Value chars = l.getModule().addGlobal(charArrType.llvmType(), name);
        org.llvm.Value str = l.getModule().addGlobal(stringType.llvmType(), name);
        org.llvm.Value [] elements = new org.llvm.Value[value.length()];
        for (int x = 0; x < value.length(); x++) {
            elements[x] = TypeRef.int64Type().constInt(value.charAt(x), false);
        }
        chars.setInitializer(charArrType.globalInitValue(l, name, elements));

        Hashtable<String, org.llvm.Value> str_args = new
        Hashtable<String, org.llvm.Value>();
        str_args.put("string", chars);
        str.setInitializer(stringType.globalInitValue(l, name, str_args));
        actual = str;
    }
    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is false.
     */
    void branchFalse(Assembly a, String lab, int free) {
        /* cannot be false */
    }

    /** Generate code to evaluate this expression and
     *  branch to a specified label if the result is true.
     */
    void branchTrue(Assembly a, String lab, int free) {
        a.emit("jmp", lab);
    }

    /** Evaluate this expression.
     */
    public Value eval(State st) {
        if (this.master != this) {
            return this.master.eval(st);
        } else if (interp_obj == null) {
            ArrayValue chars = new ArrayValue(value.length(), charArrType);
            for (int x = 0; x < value.length(); x++) {
                chars.setElem(x, new CharValue(value.charAt(x)));
            }
            ObjValue str = stringType.newObject();
            str.setField(stringType.findField("string").getOffset(), chars);
            interp_obj = str;
        }
        return interp_obj;
    }

    public org.llvm.Value llvmGen(LLVM l) {
        if (this != master) {
            return this.master.llvmGen(l);
        } else {
            return actual;
        }
    }
}
