interface Iface {
    void test(int test123);
    void other(int test123);
}

class SomeClass implements Iface {
    void other(int xyz, SomeClass FORGOTTEN) {
        System.out.println("Something else");
        System.out.println(Integer.toString(xyz));
    }
}
class SomeOtherClass implements Iface {
    void test(int xyz, int FORGOTTEN) {
        System.out.println("XXXX Does something");
        System.out.println(Integer.toString(xyz));
    }
}

class Main {
    public static void main() {
        Iface x;
        Iface y = new SomeClass();
        y.test(200);
        y.other(70);
        y = new SomeOtherClass();
        y.test(12);
        y.other(15);
    }
}
