interface If1 {
    void test(int xyz);
}

interface If2 extends If2 {
    void test(int pyq);
}

class Test implements If1 {
    void test(int pyq) {
        System.out.println(Integer.toString(pyq));
    }
}

class Main {
    public static void main() {
        If1 iface = new Test();
        iface.test(12);
    }
}
