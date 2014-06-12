// Small program for testing garbage collector:
//    Test array within object creation/deletion/gc

class TestObj {
    TestObj o;
    int x;
}

class Main {
    static void main() {
        TestObj o = new TestObj();
        o.x = 0;
        int x = 0;
        int loops = 10000;
        /* get o into old set */
        while (x < loops) {
            new int[13];
            x = x + 1;
        }
        x = 0;
        o.o = new TestObj();
        o.o.x = 9999;
        /* trigger another collection */
        while (x < loops) {
            new int[13];
            x = x + 1;
        }
        /* o should exist, but does o.o? */
        System.out.println(Integer.toString(o.o.x));
    }
}
