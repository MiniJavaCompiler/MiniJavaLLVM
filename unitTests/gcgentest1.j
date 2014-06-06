// Small program for testing gen garbage collector:
//   Test two objects with gc.  One is fixed and one is refreshed in the loop

class TestObjOne {
    int x;
    int z;
}

class TestObjTwo {
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
        loops = 1000000;
        while (x < loops) {
            ot = new TestObjTwo();
            ot.a = x;
            x = x + 1;
        }
        System.out.println(Integer.toString(oo.x));
        System.out.println(Integer.toString(ot.a));
    }
}

