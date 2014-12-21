interface If1 {
    void test(int xyz);
    void test(String overloaded);
}

class Test implements If1 {
    void test(int pyq) {
        System.out.println(pyq);
    }
    void test(String pyq) {
        System.out.println(pyq);
    }
}

class Main {
    public static void main() {
        If1 iface = new Test();
        iface.test(12);
        iface.test("Hello");
    }
}
