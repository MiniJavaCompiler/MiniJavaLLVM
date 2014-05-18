// Small program for testing garbage collector:
//

class System {
    static void out(int x);
}

class TestObjOne {
     int a;
     int b;
     int c;
     int x;
     int y;
     int z;
}


class Main {
    static void main() {
        int x;
	int loops;        
        TestObjOne oo;

        oo = new TestObjOne();
        oo.x = 42;
        oo.a = 1;

	x = 0;
        loops = 1000;
 	while (x < loops)
        {
	  oo = new TestObjOne();
          oo.a = x / 5;
          oo.b = x / 4;
          oo.c = x / 3;
          oo.x = x;
          oo.y = oo.x + x;
          oo.z = oo.y + x;
          x = x + 1;
        }
        
        System.out(oo.a);
        System.out(oo.b);
        System.out(oo.c);
        System.out(oo.x);
        System.out(oo.y);
        System.out(oo.z);
    }
}

