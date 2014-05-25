class Small {
    int small;
    int [] ary;
    public Small(int s) {
        this.small = s;
        this.ary = new int[s];
    }
}

class Main {
    public static void fill_array(int i, int [] ary) {
        int x = 0;
        while (x < ary.length) {
            ary[x] = i + x;
            x = x + 1;
        }
    }
    public static void print_array(int [] ary) {
        int x = 0;
        while (x < ary.length) {
            System.out.print(Integer.toString(ary[x]));
            System.out.print(" ");
            x = x + 1;
        }
        System.out.print("\n");
    }
    public static void main() {
        int mem = 10;
        /* this should keep allocated more than
           the possible memory space.
           Some are nulled, but garbage collection
           is the only reason it should non memory
           error. */
        int i = 0;
        while (i < mem) {
            int [] x0 = new int [7];
            fill_array(i, x0);
            Small x1 = new Small(i);
            int [] x2 = new int [6];
            fill_array(i, x2);
            int q = 5;
            int [] x3 = new int [3];
            fill_array(i, x3);
            Small x4 = new Small(i);
            int y = 2;
            int [] x5 = new int [12];
            fill_array(i, x5);
            Small x6 = new Small(i);
            int [] x7 = new int [120];
            int p = 7;

            fill_array(i, x7);

            x1 = null;
            x3 = null;
            x4 = null;

            print_array(x5);
            i = i + 1;
        }
        System.out.println("Finished Successfully");
    }
}
