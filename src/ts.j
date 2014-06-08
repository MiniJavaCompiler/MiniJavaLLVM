// Here is a small program for the Mini Java compiler:
//

class System {
    static void out(int x);
}

class Expr {
    int eval() {
        return 0;  // should never be called
    }
}

class IntExpr extends Expr {
    int value;

    static IntExpr make(int value) {
        IntExpr e;
        e       = new IntExpr();
        e.value = value;
        return e;
    }

    int eval() {
        return value;
    }
}

class AddExpr extends Expr {
    Expr left;
    Expr right;

    static AddExpr make(Expr l, Expr r) {
        AddExpr e;
        e       = new AddExpr();
        e.left  = l;
        e.right = r;
        return e;
    }

    int eval() {
        return left.eval() + right.eval();
    }
}

class Main {
    static void main() {
        Expr e;
        e = AddExpr.make(IntExpr.make(1), IntExpr.make(2));
        e = AddExpr.make(e, e);
        System.out(e.eval());
    }
}

