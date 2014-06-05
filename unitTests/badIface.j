interface Iface {
    void test(int test123);
}

class SomeClass implements Iface {
    void test(int xyz) {
        System.out.println("Does something");
    }
}
class Main {
    public static void main() {
        Iface x;
        Iface y = new Iface();
        System.out.println("Test123");
    }
}
