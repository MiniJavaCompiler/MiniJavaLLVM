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

class Main {
    static void main() {
        A Ao;
        int x;
        int y;
        int loops;
        int [] o;
        int [] old;
        old = new int[100];
        old[0] = 42;
        old[99] = 7;

        x = 0;
        loops = 10000;
        while (x < loops) {
            Ao = new A();
            x = x + 1;
            Ao.o[x % 10] = x;
        }
        Ao.o[0] = x;
        Ao.o[9] = x + 1;
        System.out.println(Integer.toString(Ao.o[0]));
        System.out.println(Integer.toString(Ao.o[9]));
        System.out.println(Integer.toString(old[0]));
        System.out.println(Integer.toString(old[99]));
    }
}

