// Small program for testing garbage collector:
//    Test basic object construction and gc relocation

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
        loops = 1000;
 	while (x < loops)
        {
	  o = new TestObj();
          o.x = x;
	  x = x + 1;
        }
        System.out.println(Integer.toString(o.x));
    }
}

