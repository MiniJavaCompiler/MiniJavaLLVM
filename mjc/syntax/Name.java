// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import checker.*;

/** Provides a representation for names, both simple and qualified.
 */
public class Name {
    private Name prefix;
    private Id id;

    /** Construct a name with a qualifying prefix.
     */
    public Name(Name prefix, Id id) {
        this.prefix = prefix;
        this.id     = id;
    }

    /** Construct a simple name with no qualifying prefix.
     */
    public Name(Id id) {
        this(null, id);
    }

    /** Returns the position of this syntactic element.
     */
    public Position getPos() {
        Name n = this;
        while (n.prefix!=null) {
            n= n.prefix;
        }
        return n.id.getPos();
    }

    /** Generate a printable representation of a name.
     */
    public String toString() {
        if (prefix==null) {
            return id.toString();
        } else {
            return prefix + "." + id;
        }
    }

    /** Lookup name as a class.
     */
    public ClassType asClass(Context ctxt) {
        if (prefix==null) {
            return ctxt.findClass(id.getName());
        }
        return null;
    }

    /** Lookup name as a value.
     */
    public FieldAccess asValue(Context ctxt, VarEnv env) {
        if (prefix==null) {
           VarEnv ve = VarEnv.find(id.getName(), env);
           if (ve!=null) {
               return new FrameAccess(ve);
           }
           ClassType cls = ctxt.getCurrClass();
           FieldEnv fe;
           if (cls!=null && (fe=cls.findField(id.getName()))!=null) {
               return new SimpleAccess(id, fe);
           }
        } else {
           Expression object = prefix.asValue(ctxt, env);
           if (object!=null) {
               return new ObjectAccess(object, id);
           }
           ClassType cls = prefix.asClass(ctxt);
           FieldEnv  fe;
           if (cls!=null && (fe=cls.findField(id.getName()))!=null) {
               return new ClassAccess(fe);
           } 
//         throw new Failure(pos,
//                    "Cannot find field " + name + " in class " + cls);
        }
        return null;
    }

    /** Lookup name as a method.
     */
    public Invocation asMethod(Context ctxt, VarEnv env, Args args) {
        if (prefix==null) {
            MethEnv menv = ctxt.getCurrClass().findMethod(id.getName());
            if (menv!=null) {
                return new ThisInvocation(id, args, menv);
            }
        } else {
            Expression object = prefix.asValue(ctxt, env);
            if (object!=null) {
                return new ObjectInvocation(object, id, args);
            }
           ClassType cls = prefix.asClass(ctxt);
           if (cls!=null) {
               return new ClassInvocation(cls, id, args);
           }
        }
        return null;
    }
}
