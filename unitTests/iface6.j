interface Iface2 {
    int test_num = 777;
    int test(int test123);
}

class Test {
    static int test_num = 888;
}

class Main {
    public static void main() {
        System.out.println(Test.test_num);
        System.out.println(Iface2.test_num);
    }
}
