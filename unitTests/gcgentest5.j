// Small program for testing garbage collector:
//    Test three array creation/deletion/gc


class Main {
    static void main() {
        int x;
        int loops;
        int [] o;
        int [] p;
        int [] old;
        old = new int[5];
        old[0] = 7;
        old[1] = 8;
        old[2] = 9;
        old[3] = 10;
        old[4] = 3;

        x = 0;
        loops = 10000;
        while (x < loops) {
            o = new int[10];
            x = x + 1;
            o[x % 10] = x;
        }
        x = 0;
        while (x < loops) {
            p = new int[10];
            x = x + 1;
            p[x % 10] = x;
        }

        o[0] = 42;
        o[9] = 8;
        p[0] = x;
        p[9] = x + 1;
        System.out.println(o[0]);
        System.out.println(o[9]);
        System.out.println(p[0]);
        System.out.println(p[9]);
        System.out.println(old[0]);
        System.out.println(old[4]);
    }
}

