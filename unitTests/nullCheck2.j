class SomeClass {
    int x;
}

class Main {
    public static void main() {
        SomeClass test = null;
        /* should fail null check */
        test.x = 7;
    }
}
