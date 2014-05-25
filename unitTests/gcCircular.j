/* utility class */
class List {
    public List next;
    public int data;
    public List(List next) {
        this.next = next;
    }
}
class Main {
    public static void cleanup(List node) {
        if (node.next != null) {
            cleanup(node.next);
        }
        node.next = null;
    }
    public static void main() {
        int mem = 10;
        int i;
        while (i < mem) {
            int [] garbage2 = new int [7];
            int j;
            while (j < 100) {
                //both of these cycles should disappear
                // after this loop and never fill up all of memory.
                List self_cycle = new List(null);
                int [] garbagex = new int [13];
                self_cycle.next = self_cycle;
                List circle_1 = new List(null);
                int [] garbagey = new int [3];
                List circle_2 = new List(null);
                circle_1.next = circle_2;
                int [] garbagez = new int [37];
                circle_2.next = circle_1;
                j = j + 1;
            }
            int [] garbageq = new int [17];
            i = i + 1;
        }
        System.out.println("Finished Successfully");
    }
}
