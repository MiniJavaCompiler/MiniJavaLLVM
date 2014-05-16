class Main {
    static public void main() {
        int p;
        p = 5;
        test();
        System.out.println(Integer.toString(p));
        if (true) {
            int p;
            p = 2;
        }
    }

    static void test() {
        int p;
        p = 0;
    }
}
