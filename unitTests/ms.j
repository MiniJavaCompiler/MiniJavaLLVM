// Small program for testing garbage collector:
//
class Expr {
    int eval() {
        return 0;  // should never be called
    }
}
class TestObj {
     int x;
     int y;
}

class Main {
    static void main() {
        int x;
	int loops;        
        TestObj o;

        o = new TestObj();
	o.x = 42;

	x = 0;
        loops = 1000000;
 	while (x < loops)
        {
	  o = new TestObj();
          o.x = x;
	  x = x + 1;
        }
        System.out.println(Integer.toString(o.x));
    }
}

