// Small program for testing garbage collector:
//    Test array within object creation/deletion/gc

class TestObj {
      int [] arr;
}

class Main {
    static void main() {
        int x;
        int loops;
        TestObj o;
        o = new TestObj();
        o.arr = new int[10];

	x = 0;
        loops = 1000;
 	while (x < loops)
        {
	  o.arr = new int[10];
	  x = x + 1;
          o.arr[0] = 42;
          o.arr[9] = x;
        }
        System.out.println(Integer.toString(o.arr[0]));
        System.out.println(Integer.toString(o.arr[9]));
  }
}
