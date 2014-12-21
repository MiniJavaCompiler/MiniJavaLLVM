// Small program for testing garbage collector:
//    Test basic array creation/deletion/gc


class Main {
    static void main() {
        int x;
        int loops;
        int [] o;
        o = new int[10];

        x = 0;
        loops = 1000;
        while (x < loops) {
            o = new int[10];
            x = x + 1;
            o[x % 10] = x;
        }
        o[0] = 42;
        o[9] = 8;
        System.out.println(o[0]);
        System.out.println(o[9]);
    }
}

