class A {
}

class B extends A {
}

class Main {
    static void main() {
        B [] b = null;
        b = new A[10];
        System.out.println("Should not be successful");
    }
}
