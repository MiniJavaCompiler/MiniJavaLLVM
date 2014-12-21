// Small program for testing garbage collector:
//    Test class with static fields

class TestObj {
    int x;
    int y;
    int z;
}

class Stat {
    static TestObj stc;
}

class Main {
    static void main() {
        int x;
        int loops;
        Stat.stc = new TestObj();
        Stat.stc.x = 5;

        x = 0;
        loops = 1000;
        while (x < loops) {
            Stat.stc = new TestObj();
            x = x + 1;
            Stat.stc.x = x;
        }

        Stat.stc.x = 42;
        System.out.println(Stat.stc.x);
    }
}

