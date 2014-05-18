// Small program for testing garbage collector:
//

class System {
    static void out(int x);
}

class TestObjOne {
     int x;
     int y;
     int z;
}

class TestObjTwo {
     int a;
     int b;
     int c;
}

class Main {
    static void main() {
        int x;
	int loops;        
        TestObjOne oo;
        TestObjTwo ot;

        oo = new TestObjOne();
	oo.x = 42;

        ot = new TestObjTwo();
        ot.a = 42;

	x = 0;
        loops = 1000;
 	while (x < loops)
        {
	  oo = new TestObjOne();
          ot = new TestObjTwo();
          oo.x = x;
          ot.a = 42;
	  x = x + 1;
        }
        System.out(oo.x);
        System.out(ot.a);
    }
}

