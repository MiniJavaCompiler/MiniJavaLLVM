class Test {
    int x;
    public Test(int test) {
        x = test;
    }
    public void test() {
        System.out.println("Test");
        System.out.println(Integer.toString(x));
    }
}

class Test2 extends Test {
    public Test2() {
        super(); /* wrong number of args */
    }
    public void test2() {
        super.x = 5;
        super.test();
        super.x = 10;
        super.test();
        System.out.println(Integer.toString(super.x));
    }
}

class Main {
    public static void main() {
        Test2 t = new Test2();
        t.test2();
    }
}
