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
import interp.*;
import util.*;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.ArrayList;

import org.llvm.BasicBlock;
import org.llvm.Builder;
import org.llvm.ExecutionEngine;
import org.llvm.GenericValue;
import org.llvm.LLVMException;
import org.llvm.Module;
import org.llvm.PassManager;
import org.llvm.Target;
import org.llvm.TypeRef;

import org.llvm.binding.LLVMLibrary.LLVMCallConv;
import org.llvm.binding.LLVMLibrary.LLVMIntPredicate;

import java.util.ArrayList;

/** Provides a representation for method environments.
 */
public final class MethEnv extends MemberEnv implements Iterable<MethEnv>,
        ListIteratorIF<MethEnv>  {
    private VarEnv    params;
    private Statement body;
    private int       slot;        // virtual function table slot number
    private int       size;        // #bytes of parameters
    private int       localBytes;  // #bytes of locals

    private MethEnv   next;
    private TypeRef   llvmFuncType;
    private org.llvm.Value functionVal;

    private boolean isMain;
    private boolean isConstructor;

    public MethEnv(boolean isConstructor,
                   Modifiers mods, Type type, Id id, VarEnv params,
                   Statement body, ClassType owner, int slot, int size,
                   MethEnv next) {
        super(mods, id, type, owner);
        this.params = params;
        this.slot   = slot;
        this.size   = size;
        this.body   = body;
        this.next   = next;
        this.llvmFuncType = null;
        this.functionVal = null;
        this.isMain = false;
        this.isConstructor = isConstructor;
    }

    public StatementExpr removeSuperConstructor() {
        if (!isConstructor) {
            return null;
        }
        Block body_block = null;
        SuperInvocation expr = null;
        ExprStmt stmt = null;
        try {
            if (body == null) {
                return null;
            } else if ((body_block = (Block)body) == null) {
                return null;
            } else if (body_block.getStmts().length == 0
                       || (stmt = (ExprStmt)body_block.getStmts()[0]) == null) {
                return null;
            } else if ((expr = (SuperInvocation)stmt.getStmtExpr()) == null) {
                return null;
            } else {
                body_block.getStmts()[0] = new Empty(getPos()); /*remove statement */
                expr.setFirst();
                return expr;
            }
        } catch (ClassCastException c) {
            return null;
        }
    }
    public void updateBody(Statement new_body) {
        this.body = new_body;
    }
    public boolean isMain() {
        return this.isMain;
    }
    public boolean isConstructor() {
        return isConstructor;
    }
    public org.llvm.Value getFunctionVal(LLVM l) {
        if (functionVal == null) {
            llvmGenTypes(l);
        }
        return functionVal;
    }
    public VarEnv getParams() {
        return params;
    }
    public Iterator<MethEnv> iterator() {
        return new ListIterator<MethEnv>(this);
    }
    public Statement getBody() {
        return body;
    }
    public MethEnv getNext() {
        return next;
    }
    /** Look for the entry corresponding to a particular identifier
     *  in a given environment.
     */
    public static MethEnv findByName(String name, MethEnv env) {
        while (env != null) {
            if (name.equals(env.id.getName())) {
                return env;
            }
            env = env.next;
        }
        return env;
    }
    public static MethEnv find(String name, VarEnv params, MethEnv env) {
        while (env != null) {
            if (name.equals(env.id.getName()) && env.eqSig(false, Type.VOID, params)) {
                return env;
            }
            env = env.next;
        }
        return env;
    }
    public static MethEnv findCall(String name, VarEnv params, MethEnv env) {
        while (env != null) {
            if (name.equals(env.id.getName()) && env.eqCall(params)) {
                return env;
            }
            env = env.next;
        }
        return env;
    }
    public static MethEnv find(MethEnv search, MethEnv env) {
        while (env != null && !search.eqMethSig(false, env)) {
            env = env.next;
        }
        return env;
    }

    /** Check the arguments for an invocation against the formal parameters.
     */
    public void checkArgs(Position pos, Context ctxt, VarEnv env, Args args) {
        VarEnv.checkArgs(pos, ctxt, env, args, params);
    }

    /** Test to see whether another signature matches the signature of this
     *  method.
     */
    public boolean eqMethSig(boolean match_return, MethEnv other) {
        return eqSig(match_return, other.type, other.params);
    }

    public boolean eqSig(boolean match_return, Type type, VarEnv params) {
        boolean return_matched = true;
        if (match_return) {
            return_matched = ((this.type == null && type == null) ||
                              (this.type != null && type != null && this.type.equal(type)));
        }
        return return_matched && VarEnv.eqTypes(this.params, params);
    }

    public boolean eqCall(VarEnv params) {
        return VarEnv.eqCall(params, this.params);
    }
    /** Static analysis on a list of method definitions.
     */
    public static void checkMethods(Context ctxt, MethEnv menv) {
        for (; menv != null; menv = menv.next) {
            menv.checkMethod(ctxt);
        }
    }

    /** Static analysis on a method body
     */
    void checkMethod(Context ctxt) {
        if (body != null) {
            ctxt.setCurrMethod(this);
            if (body.check(ctxt, params, 0) && !type.equal(Type.VOID)) {
                ctxt.report(new Failure(body.getPos(),
                                        "Method does not return a value, needs " + type));
            }
            localBytes = ctxt.getLocalBytes();
            ctxt.setCurrMethod(null);
        }
    }

    /** Construct a printable description of this environment entry for
     *  use in error diagnostics.
     */
    public String describe() {
        return "method " + id + "(" + ")";
    }

    /** Returns the slot number for this method in the vtable.
     */
    public int getSlot() {
        // TODO: Eliminate this getter
        return slot;
    }

    /** Returns the size (# bytes) of the parameters to this method.
     */
    public int getSize() {
        return size;
    }

    /** Returns the number of bytes used for local variables in this method.
     */
    public int getLocals() {
        return localBytes;
    }

    /** Add entries from this method environment to the vtable for the
     *  enclosing class.
     */
    public static void addToVTable(MethEnv env, MethEnv[] vtable) {
        for (; env != null; env = env.next) {
            if (env.slot >= 0) {
                vtable[env.slot] = env;
            }
        }
    }

    /** Generate the code for a list of method bodies.
     */
    public static void compileMethods(Assembly a, MethEnv menv) {
        for (; menv != null; menv = menv.next) {
            menv.compileMethod(a);
        }
    }

    /** Generate the code for a method body.
     */
    public void compileMethod(Assembly a) {
        if (body != null) {
            a.emitPrologue(methName(a), localBytes);
            a.emit("// Expected Arguments");
            if (!isStatic()) {
                a.emit("// THIS: " + a.indirect(a.thisOffset(size), "%ebp"));
            }
            for (VarEnv p = params; p != null; p = p.getNext()) {
                a.emit("//" + p.id.getName() + ":" + p.getType()
                       + " " + a.indirect(p.getOffset(), "%ebp"));
            }
            body.compileRet(a);
        }
    }

    public void llvmGenTypes(LLVM l) {
        llvmType();
        if (functionVal == null) {
            String functionName = owner.toString() + "_" + getName();
            if (owner.toString().equals("Main") && getName().equals("main")) {
                isMain = true;
                org.llvm.Value f = l.getModule().addFunction(functionName, llvmType());
                functionVal = f;
            } else if (isStatic() && owner.toString().equals("MJC")
                       && body == null) {
                functionName = "MJC_" + getName();
                if (functionName.equals("MJC_putc")) {
                    functionVal = l.getGlobalFn(LLVM.GlobalFn.PUTC);
                } else if (functionName.equals("MJC_allocObject")) {
                    functionVal = l.getGlobalFn(LLVM.GlobalFn.NEW_OBJECT);
                } else if (functionName.equals("MJC_allocArray")) {
                    functionVal = l.getGlobalFn(LLVM.GlobalFn.NEW_ARRAY);
                } else if (functionName.equals("MJC_die")) {
                    functionVal = l.getGlobalFn(LLVM.GlobalFn.DIE);
                } else {
                    throw new RuntimeException("Unknown Global Function " + functionName);
                }
            } else {
                org.llvm.Value f = l.getModule().addFunction(functionName, llvmType());
                functionVal = f;
            }
        }
    }

    public TypeRef llvmType() {
        if (llvmFuncType == null) {
            ArrayList<TypeRef> llvm_formals = new ArrayList<TypeRef>();
            if (!isStatic()) {
                llvm_formals.add(owner.llvmType().pointerType()); //this pointer
            }
            for (VarEnv p = params; p != null; p = p.getNext()) {
                TypeRef formal = p.getType().llvmTypeField();
                llvm_formals.add(formal);
            }
            TypeRef return_type = type.llvmTypeField();
            llvmFuncType = TypeRef.functionType(return_type, llvm_formals);
        }
        return llvmFuncType;
    }

    public org.llvm.TypeRef llvmTypeField() {
        return llvmType().pointerType();
    }

    public void llvmGenMethod(LLVM l) {
        org.llvm.Value f = functionVal;
        if (body != null) {
            f.setGC("shadow-stack");
            l.setFunction(f);
            BasicBlock entry = f.appendBasicBlock("entry");
            l.getBuilder().positionBuilderAtEnd(entry);
            l.enterScope();
            int n = 0;
            if (!isStatic()) {
                /* this pointer is not mutable and therefore no allocation is needed */
                n++;
            }
            for (VarEnv p = params; p != null; p = p.getNext()) {
                org.llvm.Value v = l.getBuilder().buildAlloca(f.getParam(n).typeOf(),
                                   p.getName());
                l.getBuilder().buildStore(f.getParam(n), v);
                l.setNamedValue(p.getType().isClass() != null, f.getParam(n).typeOf(),
                                p.getName(), v);
                l.markGCRoot(v, p.getType());
                n++;
            }
            body.llvmGen(l);
            l.leaveScope();
            if (type == Type.VOID) {
                l.getBuilder().buildRetVoid();
            } else {
                l.getBuilder().buildRet(type.llvmTypeField().constNull());
            }
        }
    }

    /** Generate code for a call to this method, assuming that the receiving
     *  object, if any, is in register zero, and also returning the result,
     *  if any, in the specified free register.  We assume also that any other
     *  registers that were in use have been spilled onto the stack before
     *  this method is invoked.
     */
    public void compileInvocation(Assembly a, Expression object, Args args,
                                  int free) {
        int argReg = 0;
        int orig_free = free;
        TmpAccess t = new TmpAccess(getPos(), Type.VOID);
        int nargs = 0;
        if (!isStatic()) {
            t.setTmp(a, free);
            object.compileExpr(a, free);
            a.spill(++free);
            nargs++;
        }
        for (; args != null; args = args.getNext()) {
            args.getArg().compileExpr(a, free);
            a.spill(++free);
            nargs++;
        }
        free = a.spillAll(free);
        if (isStatic()) {
            a.call(methName(a), orig_free, size);
        } else {
            InterfaceType iface = owner.isInterface();
            t.compileExpr(a, free);
            if (iface == null) {
                a.emit("movl", a.indirect(0, a.reg(free)), a.reg(free));
            } else {
                a.emit("movl", a.vtAddr(iface), a.reg(free));
            }
            a.emit("movl", a.indirect(a.vtOffset(slot), a.reg(free)), a.reg(free));
            a.emit("//Calling " + methName(a));
            a.call(a.aindirect(a.reg(free)), orig_free, size);
        }
        a.unspillTo(free - nargs, orig_free);
    }

    /** Mangle the class and method name to make a label for the entry
     *  point to this method.
     */
    public String methName(Assembly a) {
        ArrayList<String> items = new ArrayList<String>();
        items.add(owner.toString());
        items.add(getName());
        for (VarEnv p = params; p != null; p = p.getNext()) {
            items.add(p.getType().toString());
        }

        return a.mangle(items.toArray(new String[0]));
    }

    /** Call this method with a particular object and set of arguments.
     */
    public Value call(State st, ObjValue obj, Args args) {
        if (isStatic()) {
            Args.push(st, args);
            return st.call(this);
        } else {
            obj.checkNull();
            st.push(obj);
            Args.push(st, args);
            InterfaceType iface = owner.isInterface();
            if (iface == null) {
                return obj.call(st, slot);
            } else {
                return iface.call(st, slot);
            }
        }
    }

    /** Execute a particular method, assuming that the stack frame
     *  has already been set up as necessary.  If the method body
     *  is null, we check to see if there is a corresponding primitive
     *  function and execute that instead.  For the time being, the
     *  only primitive is System.out.
     */
    public Value run(State st) {
        if (body != null) {
            return body.exec(st);
        }
        if (owner.toString().equals("MJC") && isStatic()) {
            if (getName().equals("putc")) {
                System.out.print(st.getFrame(8).getChar());
                return Value.NULL;
            } else if (getName().equals("die")) {
                return new DieInvocation(id.getPos()).eval(st);
            }
        }
        Interp.abort("Cannot execute method " + getName()
                     + " in class " + owner);
        return null;
    }

}
