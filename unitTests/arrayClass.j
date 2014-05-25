class Element {
    int x;
    int y;
    int z;
    Element other;
}

class Main {
    static void main() {
        int elem_size = 10;
        Element [] elems = new Element[elem_size];

        int x = 0;
        do {
            Element e = elems[x] = new Element();
            e.x = x * 5;
            e.y = x * 7;
            e.z = x * -1;
            x = x + 1;
        } while (x < elem_size);

        System.out.println(Integer.toString(elems[5].x));
        System.out.println(Integer.toString(elems[7].y));
        System.out.println(Integer.toString(elems[2].z));
    }
}
