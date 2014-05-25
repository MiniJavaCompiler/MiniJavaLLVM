// Small program for testing garbage collector:
//  Test gc with derived class

class TestObjOne {
    int x;
    int z;
}

class TestObjTwo extends TestObjOne {
    int a;
    int c;
}

class Main {
    static void main() {
        int x;
        int loops;
        TestObjOne oo;
        TestObjTwo ot;

        oo = new TestObjOne();
        oo.x = 42;

        ot = new TestObjTwo();
        ot.a = 42;

        x = 0;
        loops = 10000;
        while (x < loops) {
            oo = new TestObjOne();
            ot = new TestObjTwo();
            oo.x = x;
            ot.a = 42;
            x = x + 1;
        }
        System.out.println(Integer.toString(oo.x));
        System.out.println(Integer.toString(ot.a));
    }
}

