// Small program for testing garbage collector:
//

class TestObjOne {
    int a;
    int b;
}

class TestObjTwo {
    int c;
    int d;
}
class TestObjThree {
    int e;
    int f;
}
class TestObjFour {
    int g;
    int h;
}
class Main {
    static void main() {
        int x;
        int loops;
        TestObjOne oo;
        TestObjTwo ot;
        TestObjThree ott;
        TestObjFour of;

        oo = new TestObjOne();
        ot = new TestObjTwo();
        ott = new TestObjThree();
        of = new TestObjFour();
        oo.a = 42;
        oo.b = 1;

        x = 0;
        loops = 1000;
        while (x < loops) {
            oo = new TestObjOne();
            ot = new TestObjTwo();
            oo.a = x / 5;
            oo.b = x / 4;
            ot.c = x / 3;
            ot.d = x / 2;
            of.g = oo.a + x;
            of.h = ot.c + x;
            x = x + 1;
        }

        System.out.println(oo.a);
        System.out.println(oo.b);
        System.out.println(ot.c);
        System.out.println(ot.d);
        System.out.println(of.g);
        System.out.println(of.h);
    }
}

