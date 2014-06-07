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

/** Provides a representation for expressions that can appear
 *  in a statement.
 */
public class CastExpr extends Expression {
    Expression needsCast;
    Type castType;
    public CastExpr(Position pos, Type castType, Expression needsCast) {
        super(pos);
        this.castType = castType;
        this.needsCast = needsCast;
    }

    public Type typeOf(Context ctxt, VarEnv env)
    throws Diagnostic {
        castType = castType.check(ctxt);
        needsCast.typeOf(ctxt, env);
        return castType;
    }

    public org.llvm.Value llvmGen(LLVM l) {
        if (castType.equal(Type.INT)) {
            return l.getBuilder().buildTrunc(needsCast.llvmGen(l),
                                             castType.llvmTypeField(), "int_trunc");
        } else if (castType.equal(Type.LONG)) {
            return l.getBuilder().buildSExt(needsCast.llvmGen(l),
                                            castType.llvmTypeField(), "long_extend");
        } else {
            return l.getBuilder().buildBitCast(needsCast.llvmGen(l),
                                               castType.llvmTypeField(), "bit_cast");
        }
    }

    public void compileExpr(Assembly a, int free) {
        /* no need for casts in x86 */
        needsCast.compileExpr(a, free);
    }
    public Value eval(State st) {
        return needsCast.eval(st);
    }
}
