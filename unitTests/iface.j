interface Iface {
    void test(int test123);
    void other(int test123);
}

class SomeClass implements Iface {
    void ignored() {

    }
    void test(int xyz) {
        System.out.println("Does something");
        System.out.println(Integer.toString(xyz));
    }
    void ignored2() {

    }
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
    void ignored() {

    }
    void other(int xyz) {
        System.out.println("XXX Something else");
        System.out.println(Integer.toString(xyz));
    }
    void ignored2() {

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
