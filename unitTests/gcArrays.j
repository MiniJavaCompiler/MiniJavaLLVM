class Element {
    int x;
    int y;
    int z;
}

class Main {
    static void set_array(Element [] ary, int n) {
        int x = 0;
        while (x < ary.length) {
            ary[x] = new Element();
            ary[x].x = n;
            ary[x].y = n * 2;
            ary[x].z = n * 3;
            x = x + 1;
        }
    }

    static void check_array(Element [] ary, int n) {
        int x = 0;
        while (x < ary.length) {
            if (ary[x].x != n ||  ary[x].y != n * 2 || ary[x].z != n * 3) {
                System.out.println("Value mismatch");
            }
            x = x + 1;
        }
    }
    static void array_test(int x, Element [] elems1, Element [] elems2,
                           Element [] elems3) {
        set_array(elems1, x % 7);
        new Element[37];
        set_array(elems2, x % 123);
        new Element[37];
        set_array(elems3, x % 105);
        new Element[37];
        check_array(elems1, x % 7);
        new Element[37];
        check_array(elems2, x % 123);
        new Element[37];
        check_array(elems3, x % 105);
    }

    static void main() {
        int repitition = 50;
        int x = 0;
        while (x < repitition) {
            array_test(x, new Element[7], new Element[17], new Element[23]);
            new Element[37];
            x = x + 1;
        }
        System.out.println("Completed Successfully");
        System.out.println(x);
    }
}
