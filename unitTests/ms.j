// Small program for testing garbage collector:
//

class System {
    static void out(int x);
}

class TestObj {
     int x;
     int y;
     int z;
}

class Main {
    static void main() {
        int x;
	int loops;        
        TestObj o;

        o = new TestObj();
	o.x = 42;

	x = 0;
        loops = 1000;
 	while (x < loops)
        {
	  o = new TestObj();
          o.x = x;
	  x = x + 1;
        }
        System.out(o.x);
    }
}

