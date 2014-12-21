interface Iface {
    static void test(int test123) {
        System.out.println(test123);
    }
}


class Main {
    public static void main() {
        Iface.test(123);
    }
}
