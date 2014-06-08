/* utility class */
class List {
    public List next;
    public int data;
    public List(List next, int data) {
        this.next = next;
        this.data = data;
    }
}

class TestObjOne {
    int x;
    int z;
}

class A {
    int x;
    int y;
    int z;
    int [] o;
    A() {
        o = new int[10];
    }
}


/* benchmark tests */
/* all benchmarks implement static void run() as entry point */
class benchSimpleObj {
    static void run() {
        int x;
        int loops;
        TestObjOne oo;

        oo = new TestObjOne();
        oo.x = 42;

        x = 0;
        loops = 100000;
        while (x < loops) {
            oo = new TestObjOne();
            oo.x = x;
            x = x + 1;
        }
    }
}

class benchArrayCreate {
    static void run() {
        int x;
        int loops;
        int [] o;
        o = new int[10];

        int [] old;
        old = new int[10];
        old[0] = 77;
        old[9] = 770;

        x = 0;
        loops = 100000;
        while (x < loops) {
            o = new int[10];
            x = x + 1;
            o[x % 10] = x;
        }
    }
}

class benchArrayStore {
    static void run() {
        int x;
        int y;
        int oloops = 10000;
        int iloops = 10;
        int [] o;
        o = new int[10];

        int [] old;
        old = new int[10];
        old[0] = 77;
        old[9] = 770;

        x = 0;
        while (x < oloops) {
            y = 0;
            while (y < iloops) {
                o = new int[10];
                o[y] = x;
                y = y + 1;
            }
            x = x + 1;
        }
    }
}

class benchArrayAndObj {
    static void run() {
        A Ao;
        int x;
        int y;
        int loops;
        int [] o;
        int [] old;
        old = new int[10];
        old[0] = 42;
        old[9] = 7;

        x = 0;
        loops = 100000;
        while (x < loops) {
            Ao = new A();
            x = x + 1;
            Ao.o[x % 10] = x;
        }
    }
}

class benchList {
    static void run() {
        int mem = 100;
        /* this should keep allocated more than
           the possible memory space.
           Some are nulled, but garbage collection
           is the only reason it should non memory
           error. */
        List l = null;
        List prev = null;
        int i = 0;
        while (i < mem) {
            int [] garbage2 = new int [7];
            int j = 0;
            while (j < 15) {
                l = new List(prev, j);
                int [] garbage = new int [13];
                prev = l;
                j = j + 1;
            }
            i = i + 1;
        }
    }
}





class Main {
    public static void main() {

        // set loop to max value - x86 has memory size limit
        // so loop has to remain fairly small to work on x86
        int loop = 10;  //
        int x = 0;

        while (x < loop) {
            benchList test1 = new benchList();
            test1.run();
            test1 = null;  // enable garbage collector

            benchSimpleObj test2 = new benchSimpleObj();
            test2.run();
            test2 = null;  // enable garbage collector

            benchArrayCreate test3 = new benchArrayCreate();
            test3.run();
            test3 = null;

            benchArrayStore test4 = new benchArrayStore();
            test4.run();
            test4 = null;

            benchArrayAndObj test5 = new benchArrayAndObj();
            test5.run();
            test5 = null;

            x = x + 1;
        }
        System.out.println("Benchmarks Completed Successfully");
    }
}
