class SomeClass {
    SomeClass deep;
    int x;
    public SomeClass() {
        deep = null;
    }
}

class Main {
    public static void main() {
        SomeClass test = new SomeClass();
        /* should fail null check */
        test.deep.x = 7;
    }
}
