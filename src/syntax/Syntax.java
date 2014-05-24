// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.*;
import codegen.*;
import org.llvm.*;

/** Provides a representation for syntactic elements, each of which is
 *  annotated with a position in the input file.
 */
public abstract class Syntax {
    protected Position pos;

    public Syntax(Position pos) {
        this.pos = pos;
    }

    /** Returns the position of this syntactic element.
     */
    public Position getPos() {
        return pos;
    }

    public org.llvm.Value setLLVMMetaData(LLVM l, org.llvm.Value instr) {
        org.llvm.Value meta = Value.MDNode(
        new Value[] {
            TypeRef.int32Type().constInt(pos.getRow(), false),
            TypeRef.int32Type().constInt(pos.getColumn(), false),
            l.getMetaContext(),
            new org.llvm.Value(null)
        });
        instr.setMetadata(0 /*kind ? */, meta);
        return instr;
    }
}
