interface Iface2 {
    int test(int test123);
}

interface Iface extends Iface2 {
    int other(int test123);
}

class SomeClass implements Iface {
    int test(int xyz) {
        System.out.println("Does something");
        System.out.println(xyz);
        return 12834;
    }
    int other(int xyz) {
        System.out.println("Something else");
        System.out.println(xyz);
        return 12738;
    }
}

class SomeOtherClass implements Iface, Iface2 {
    int test(int xyz) {
        System.out.println("XXXX Does something");
        System.out.println(xyz);
        return 123;
    }
    int other(int xyz) {
        System.out.println("XXX Something else");
        System.out.println(xyz);
        return 100;
    }
}


class Main {
    public static void main() {
        Iface x;
        Iface2 y = new SomeClass();
        System.out.println(y.test(200));
        x = new SomeOtherClass();
        System.out.println(x.other(15));
    }
}
