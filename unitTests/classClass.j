class TestObjOne {
    int x;
    int z;
}

class TestObjTwo extends TestObjOne {
    int p;
    int q;
}

class Main {
    static void main() {
        TestObjOne oo;
        TestObjTwo pp;
        oo = new TestObjOne();
        pp = new TestObjTwo();
        System.out.println("Class Name");
        System.out.println(oo.getClass().getName());
        System.out.println(oo.getClass().isInstance(pp));
        System.out.println(pp.getClass().isInstance(oo));
    }
}

