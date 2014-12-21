interface Iface2 {
    void test(int test123);
}

interface Iface {
    void other(int test123);
}

class SomeClass implements Iface, Iface2 {
    void ignored() {

    }
    void test(int xyz) {
        System.out.println("Does something");
        System.out.println(xyz);
    }
    void ignored2() {

    }
    void other(int xyz) {
        System.out.println("Something else");
        System.out.println(xyz);
    }
}

class SomeOtherClass implements Iface, Iface2 {
    void test(int xyz) {
        System.out.println("XXXX Does something");
        System.out.println(xyz);
    }
    void ignored() {

    }
    void other(int xyz) {
        System.out.println("XXX Something else");
        System.out.println(xyz);
    }
    void ignored2() {

    }
}


class Main {
    public static void main() {
        Iface x;
        Iface2 y = new SomeClass();
        y.test(200);
        x = new SomeOtherClass();
        x.other(15);
    }
}
