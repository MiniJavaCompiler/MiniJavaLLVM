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
import java.util.Iterator;
import java.util.Collections;
import org.llvm.TypeRef;
import java.util.ArrayList;

/** Provides a representation for local variable declarations in a block.
 */
public class LocalVarDecl extends Statement {
    private Type type;
    private VarDecls varDecls;
    private Block block;
    public LocalVarDecl(Position pos, Type type, VarDecls varDecls) {
        super(pos);
        this.type     = type;
        this.varDecls = varDecls;
    }

    public boolean check(Context ctxt, VarEnv env, int frameOffset) {
        return check(ctxt, env, frameOffset,
                     Collections.<Statement>emptyList().iterator());
    }
    /** Check whether this statement is valid and return a boolean
     *  indicating whether execution can continue at the next statement.
     */
    public boolean check(Context ctxt, VarEnv env, int frameOffset,
                         Iterator<Statement> iter) {
        ArrayList<Statement> assigns = new ArrayList<Statement>();

        type = type.check(ctxt);
        if (type != null) {
            int size = type.size();
            for (VarDecls vs = varDecls; vs != null; vs = vs.getNext()) {
                try {
                    Type init = null;
                    if (vs.getInitExpr() != null) {
                        init = vs.getInitExpr().typeOf(ctxt, env);
                    }
                    if (VarEnv.find(vs.getId().getName(), env) != null) {
                        ctxt.report(new Failure(pos,
                                                "Repeated definition for variable " + vs.getId()));
                    } else if (init != null && !type.isSuperOf(init)) {
                        ctxt.report(new Failure(pos, "Cannot initialize value of type " + type +
                                                " to variable of type " + init));
                    } else {
                        frameOffset -= size;
                        VarEnv prev_env = env;
                        env = new VarEnv(vs.getId(), type, frameOffset, env);
                        Expression e;
                        if (vs.getInitExpr() != null) {
                            e = vs.getInitExpr();
                        } else if (type.isClass() != null) {
                            e = new CastExpr(vs.getId().getPos(), type,
                                             new NullLiteral(vs.getId().getPos()));
                        } else {
                            e = new CastExpr(vs.getId().getPos(), type, new IntLiteral(vs.getId().getPos(),
                                             0));
                        }
                        Statement s = new ExprStmt(vs.getId().getPos(),
                                                   new AssignExpr(vs.getId().getPos(), new FrameAccess(env), e));
                        assigns.add(s);
                        s.check(ctxt, prev_env, frameOffset);
                    }
                } catch (Diagnostic d) {
                    ctxt.report(d);
                }
            }
            ctxt.reserveSpace(frameOffset);
        }
        block = new Block(pos, assigns.toArray(new Statement[0]));
        if (!iter.hasNext()) {
            ctxt.report(new Failure(pos, "Declarations have no use"));
            return true;
        } else {
            Statement s = iter.next();
            return s.check(ctxt, env, frameOffset, iter);
        }
    }

    /** Emit code to execute this statement.
     */
    void compile(Assembly a) {
        block.compile(a);
    }

    public void llvmGen(LLVM l) {
        org.llvm.Builder b = l.getBuilder();
        for (VarDecls vs = varDecls; vs != null; vs = vs.getNext()) {
            org.llvm.Value v = b.buildAlloca(type.llvmTypeField(), vs.getId().getName());
            l.setNamedValue(type.isClass() != null, type.llvmTypeField(),
                            vs.getId().getName(), v);
            l.markGCRoot(v, type);
        }
        block.llvmGen(l);
    }


    /** Execute this statement.  If the statement is terminated by a
     *  return statement, return the corresponding value.  Otherwise,
     *  a null indicates that no result was returned.
     */
    public Value exec(State st) {
        block.exec(st);
        return null;
    }
}
