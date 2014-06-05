// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import checker.*;
import compiler.*;
import codegen.*;
import interp.*;
import syntax.*;

import java.util.Arrays;
import org.llvm.TypeRef;
import java.util.ArrayList;
import org.llvm.Builder;
import java.util.Hashtable;

import org.llvm.binding.LLVMLibrary.LLVMLinkage;

/** Provides a representation for class types.
 */
public final class InterfaceType extends ClassType {
    private ArrayList<ClassType> implements_me;
    public InterfaceType(Modifiers mods, Id id, Type extendsType, Decls decl) {
        super(mods,
              id,
              extendsType,
              new Type[0], /* interfaces do not implement any types */
              decl);
        implements_me = new ArrayList<ClassType>();
    }

    public InterfaceType isInterface() {
        return this;
    }
    public void registerImplement(ClassType c) {
        implements_me.add(c);
    }
    public void concreteMethods(Context ctxt) {
        MethEnv methods = getMethods();
        if (methods == null) {
            return;
        }
        Position pos = getId().getPos();
        for (MethEnv menv = methods; menv != null; menv = menv.getNext()) {
            if (menv.getBody() == null) {
                Statement else_stmt;
                if (menv.getType().equal(Type.VOID)) {
                    else_stmt = new Empty(pos);
                } else {
                    else_stmt = new Return(pos, menv.getType().defaultExpr(pos));
                }
                Expression classid =
                    new ObjectAccess(new CastExpr(pos, ctxt.findClass("Object"),
                                                  new This(pos)), new Id(pos, "classId"));
                for (ClassType t : implements_me) {
                    if (t.isInterface() == null) {
                        Args arg = null;
                        for (VarEnv v = menv.getParams(); v != null; v = v.getNext()) {
                            arg = new Args(new NameAccess(new Name(v.getId())), arg);
                        }
                        StatementExpr invoke =
                            new ObjectInvocation(new CastExpr(pos, t, new This(pos)), menv.getId(), arg);

                        Statement stmt;
                        if (menv.getType() == Type.VOID) {
                            stmt = new ExprStmt(pos, invoke);
                        } else {
                            stmt = new Return(pos, invoke);
                        }
                        else_stmt =
                            new IfThenElse(pos,
                                           new EqualExpr(pos, classid, new IntLiteral(pos, t.getClassId())),
                                           stmt,
                                           else_stmt);
                    }
                }
                menv.updateBody(else_stmt);
            }
        }
    }
    public boolean isSuperOf(Type t) {
        if (t.equal(Type.NULL)) {
            return true;
        }
        ClassType that = t.isClass();
        if (that != null) {
            return that.possibleInstances().contains(this);
        } else {
            return false;
        }
    }
}
