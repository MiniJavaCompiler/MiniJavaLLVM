// Small program for testing garbage collector:
//    Test nested loop for gc gen 

class Main {
    static void main() {
        int x;
	int y;
        int loops;
        int [] o;
	int [] p;
        int [] old;
        old = new int[100];
        old[0] = 7;
        old[1] = 8;
        old[2] = 9;
        old[3] = 10;
        old[99] = 3;

        x = 0;
        loops = 100;
        while (x < loops) {
            o = new int[10];
            x = x + 1;
            o[x % 10] = x;

	    y = 0;
	    while (y < loops) {
            	  p = new int[10];
            	  y = y + 1;
            	  p[y % 10] = y;
            }
        }


        o[0] = 42;
        o[9] = 8;
        p[0] = x;
        p[9] = x + 1;
        System.out.println(Integer.toString(o[0]));
        System.out.println(Integer.toString(o[9]));
        System.out.println(Integer.toString(p[0]));
        System.out.println(Integer.toString(p[9]));
        System.out.println(Integer.toString(old[0]));
        System.out.println(Integer.toString(old[99]));
    }
}

