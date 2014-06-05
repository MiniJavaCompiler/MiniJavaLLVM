interface Iface {
    void test(int test123);
    void other(int test123);
}

class SomeClass implements Iface {
    void other(int xyz) {
        System.out.println("Something else");
        System.out.println(Integer.toString(xyz));
    }
}

class SomeOtherClass implements Iface {
    void test(int xyz) {
        System.out.println("XXXX Does something");
        System.out.println(Integer.toString(xyz));
    }
}

class Main {
    public static void main() {

    }
}
