// Small program for testing garbage collector:
//    Test nested loop for gc gen

class A {
    int x;
    int y;
    int z;
    int [] o;
    A() {
        o = new int[100];
    }
}

class B {
    int [] b;
    B() {
        b = new int[100];
    }

    int getBB() {
        int x;
        int loops;
        x = 0;
        loops = 100;
        while (x < loops) {
            b[x] = x;
            x = x + 1;
        }
        return b[99];
    }
}

class Main {
    static void main() {
        A Ao;
        B Bo;
        int x;
        int y;
        int loops;
        int [] o;
        int [] old;
        old = new int[100];
        old[0] = 42;
        old[99] = 7;

        Bo = new B();

        x = 0;
        loops = 10000;
        while (x < loops) {
            Ao = new A();
            x = x + 1;
            Ao.o[x % 10] = x;
        }

        old[0] = Bo.getBB();
        Ao.o[0] = x;
        Ao.o[9] = x + 1;
        System.out.println(Ao.o[0]);
        System.out.println(Ao.o[9]);
        System.out.println(old[0]);
        System.out.println(old[99]);
    }
}

