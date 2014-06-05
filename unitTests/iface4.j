interface Iface {
    static void test(int test123) {
        System.out.println(Integer.toString(test123));
    }
}


class Main {
    public static void main() {
        Iface.test(123);
    }
}
