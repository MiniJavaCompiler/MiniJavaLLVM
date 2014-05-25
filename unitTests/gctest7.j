// Small program for testing garbage collector:
//    Test two array creation/deletion/gc


class Main {
    static void main() {
        int x;
        int loops;
        int [] o;
        int [] p;
        o = new int[10];
        p = new int[5];
        p[0] = 7;
        p[1] = 8;
        p[2] = 9;
        p[3] = 10;
        p[4] = 3;

        x = 0;
        loops = 1000;
        while (x < loops) {
            o = new int[10];
            x = x + 1;
            o[x % 10] = x;
        }
        o[0] = 42;
        o[9] = 8;
        System.out.println(Integer.toString(o[0]));
        System.out.println(Integer.toString(o[9]));
        System.out.println(Integer.toString(p[0]));
        System.out.println(Integer.toString(p[4]));
    }
}

