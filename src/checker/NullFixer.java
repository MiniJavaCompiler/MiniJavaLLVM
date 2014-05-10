package checker;
import syntax.*;
import fr.umlv.sprintabout.VisitorGenerator;

public abstract class NullFixer {
    private Type t;
    public void setType(Type t) {
        this.t = t;
    }
    public void value(NullLiteral n) {
        n.forceNull(t);
    }
    public void value(BinaryOp n) {
        NullFixer.FixNulls(t, n.getLeft());
        NullFixer.FixNulls(t, n.getRight());
    }
    public void value(AssignExpr n) {
        NullFixer.FixNulls(t, n.getRHS());
    }
    public abstract void valueAppropriate(Syntax a);

    public static void FixNulls(Type t, Syntax s) {
        VisitorGenerator vg = new VisitorGenerator();
        NullFixer visitor = vg.createVisitor(NullFixer.class);
        visitor.setType(t);
        visitor.valueAppropriate(s);
    }
}
