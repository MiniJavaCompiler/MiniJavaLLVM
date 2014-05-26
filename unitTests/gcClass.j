class Element {
    int x;
    int y;
    int z;
}

class Main {
    static Element create_elem(int n) {
        Element temp = new Element();
        temp.x  = n + 5;
        temp.y = n + 2;
        temp.z = n + 7;
        return temp;
    }

    static void main() {
        int repitition = 500;
        int x = 0;
        while (x < repitition) {
            int y = create_elem(5).x + create_elem(7).y + create_elem(2).z;
            y = y + 1;
            x = x + 1;
        }
        System.out.println("Completed Successfully");
        System.out.println(Integer.toString(x));
    }
}
