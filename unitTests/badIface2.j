interface Iface {
    void test(int test123);
    void other(int test123);
}

class SomeClass implements SomeOtherClass {
    void test(int xyz) {
        System.out.println("Does something");
        System.out.println(xyz);
    }
    void other(int xyz) {
        System.out.println("Something else");
        System.out.println(xyz);
    }
}
class SomeOtherClass implements Iface {
    void test(int xyz) {
        System.out.println("XXXX Does something");
        System.out.println(xyz);
    }
    void other(int xyz) {
        System.out.println("XXX Something else");
        System.out.println(xyz);
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
