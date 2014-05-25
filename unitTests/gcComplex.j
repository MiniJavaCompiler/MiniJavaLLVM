/* utility class */
class List {
    public List next;
    public int data;
    public List(List next, int data) {
        this.next = next;
        this.data = data;
    }
}
class Main {
    public static void cleanup(int depth, List node) {
        if (node.next != null) {
            cleanup(depth + 1, node.next);
        }
        node.next = null;
    }
    public static void main() {
        int mem = 100;
        /* this should keep allocated more than
           the possible memory space.
           Some are nulled, but garbage collection
           is the only reason it should non memory
           error. */
        List prev = null;
        int i = 0;
        while (i < mem) {
            int [] garbage2 = new int [7];
            int j = 0;
            while (j < 15) {
                List l = new List(prev, j);
                int [] garbage = new int [13];
                prev = l;
                j = j + 1;
            }
            cleanup(0, prev);
            i = i + 1;
        }
        System.out.println("Completed Successfully");
    }
}
