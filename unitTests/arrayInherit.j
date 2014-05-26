class A {
}

class B extends A {
}

class Main {
    static void main() {
        A [] a = new A[10];
        B [] b = new B[10];
        a = b;
        A [] a2 = new B[10];
        System.out.println("Completed Successfully");
    }
}
