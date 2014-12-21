class MJC {
    static void putc(char s);
    static CPOINTER allocObject(int bytes);
    static CPOINTER allocArray(int bytes, int len);
    static void die();
}

class Exception {
    public static void throw (String msg) {
        System.out.print("EXCEPTION: ");
        System.out.println(msg);
        MJC.die();
    }
    public static void throwLoc(String file, int lineno, String message) {
        String [] items = new String[] {
            file,
            Integer.toString(lineno),
            message
        };
        Exception.throw(String.format("%:% %", items));
    }
}

interface Comparable {
    int compareTo(Comparable o);
    int compareToKey();
}

class Arrays {
    public static int binarySearchRange(Comparable[] ary, Comparable key, int low,
                                        int high) {
        do {
            int mid = low + (high - low) / 2;
            int cmp = ary[mid].compareTo(key);

            if (cmp > 0) {
                high = mid - 1;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        } while (high > low);

        if (ary[high].compareTo(key) == 0) {
            return high;
        } else {
            /* not found */
            return -1;
        }
    }
    public static int binarySearch(Comparable[] ary, Comparable key) {
        return binarySearchRange(ary, key, 0, ary.length - 1);
    }
    public static void swap(Comparable [] ary, int a, int b) {
        Comparable tmp = ary[a];
        ary[a] = ary[b];
        ary[b] = tmp;
    }
    private static int quicksort_partition(Comparable [] ary, int low, int high) {
        int pivot = low + (high - low) / 2;
        Comparable pval = ary[pivot];
        int storeIndex = low;
        int i = low;
        swap(ary, pivot, high);

        while (i < high) {
            if (ary[i].compareTo(pval) < 0) {
                swap(ary, i, storeIndex);
                storeIndex = storeIndex + 1;
            }
            i = i + 1;
        }
        swap(ary, storeIndex, high);
        return storeIndex;
    }
    private static void quicksort(Comparable [] ary, int low, int high) {
        if (low < high) {
            int partition = quicksort_partition(ary, low, high);
            quicksort(ary, low, partition - 1);
            quicksort(ary, partition + 1, high);
        }
    }
    public static void sort(Comparable [] ary) {
        quicksort(ary, 0, ary.length - 1);
    }
}

class Char {
    private char c;
    public Char(char c) {
        this.c = c;
    }
    public static String toString(char c) {
        return String.makeStringChar(new char[] {c});
    }
    public String toString() {
        return Char.toString(c);
    }
}

class Class implements Comparable {
    private Integer [] instancesof;
    private String name;
    private int classRefId;
    public Class(String name, int classRefId, int [] instancesof) {
        this.name = name;
        this.classRefId = classRefId;
        this.instancesof = Integer.boxInt(instancesof);
        Arrays.sort(this.instancesof);
    }
    public int compareToKey() {
        return this.classRefId;
    }
    public int compareTo(Comparable o) {
        return this.compareToKey() - o.compareToKey();
    }
    public String getName() {
        return name;
    }
    public boolean isInstance(Object obj) {
        return classRefId == obj.classId
               || !(Arrays.binarySearch(instancesof, new Integer(obj.classId)) < 0);
    }
    public int getClassRefId() {
        return classRefId;
    }
}

class ClassPool {
    private static ClassPool classPool = null;
    private Class [] pool;
    private int used;
    private ClassPool() {
        pool = new Class[16];
        used = 0;
    }
    public static ClassPool getInstance() {
        if (classPool == null) {
            classPool = new ClassPool();
        }
        return classPool;
    }
    public static void addClasses(String [] names, int [] classRefIds,
                                  int [][] instancesof) {
        int i = 0;
        ClassPool pool = ClassPool.getInstance();
        while (i < names.length) {
            pool.addClass(new Class(names[i], classRefIds[i], instancesof[i]));
            i = i + 1;
        }
    }
    public void addClass(Class c) {
        int i = 0;
        if (!(pool.length > used)) {
            int size = pool.length * 2;
            Class [] new_pool = new Class[size];
            int x = 0;
            while (x < used) {
                new_pool[x] = pool[x];
                x = x + 1;
            }
            pool = new_pool;
        }
        /* place at end of list */
        pool[used] = c;

        /* perform insertion sort to sort the new element*/
        i = used;
        while (i > 0 && pool[i - 1].compareTo(c) > 0) {
            pool[i] = pool[i - 1];
            i = i - 1;
        }
        pool[i] = c;
        used = used + 1;
    }
    public Class findClass(int classId) {
        int i = 0;
        int ix = 0;
        ix = Arrays.binarySearchRange(pool, new Integer(classId), 0, used - 1);
        if (ix < 0) {
            return null;
        } else {
            return pool[ix];
        }
    }
    public void printDebug() {
        int x = 0;
        System.out.println("Size");
        System.out.println(pool.length);

        System.out.println("Used");
        System.out.println(used);

        while (x < used) {
            String [] items = new String[] {
                pool[x].getName(),
                Integer.toString(pool[x].getClassRefId())
            };
            System.out.println(String.format("Name: % Id: %", items));
            x = x + 1;
        }
    }
}

abstract class Object {
    protected int classId;
    public String toString() {
        return "<SomeObject>";
    }
    public static Object nullCheck(String filename, int lineno, Object o) {
        if (o == null) {
            Exception.throwLoc(filename, lineno, "Null Reference");
        }
        return o;
    }
    public Class getClass() {
        return ClassPool.getInstance().findClass(classId);
    }
    public int getClassId() {
        return classId;
    }
}

class Array {
    public static boolean inBounds(int length, int index) {
        return index < length && index > -1;
    }
    public static boolean boundsCheck(String filename, int lineno, int length,
                                      int index) {
        if (inBounds(length, index)) {
            return true;
        } else {
            String msg = "";
            String [] items = new String[] {
                Integer.toString(index),
                Integer.toString(length)
            };
            Exception.throwLoc(filename, lineno,
                               String.format("Array access out of bounds (index %, length %).", items));
            return false;
        }
    }
}

class String {
    private char [] string;
    public int length() {
        return string.length;
    }
    public static String makeStringChar(char [] c) {
        int x;
        String s;
        s = new String();
        s.string = new char[c.length];
        x = 0;
        while (x < c.length) {
            s.string[x] = c[x];
            x = x + 1;
        }
        return s;
    }
    public String toString() {
        return this;
    }
    public static String format(String format, String [] items) {
        String result = "";
        int x = 0;
        int current_replace = 0;
        while (x < format.length()) {
            char c = format.charAt(x);
            if (c == '%') {
                if (current_replace < items.length) {
                    result = result.append(items[current_replace]);
                    current_replace = current_replace + 1;
                } else {
                    Exception.throw("Too few strings provided to format.");
                }
            } else {
                result = result.appendChar(c);
            }
            x = x + 1;
        }
        return result;
    }
    public String appendChar(char c) {
        char [] result = new char[this.length() + 1];
        int x = 0;
        while (x < this.length()) {
            result[x] = this.string[x];
            x = x + 1;
        }
        result[x] = c;
        return String.makeStringChar(result);
    }

    public String append(String s) {
        String appended;
        int new_length;
        new_length = s.length() + this.length();
        appended = new String();
        appended.string = new char[new_length];
        int x;
        x = 0;
        while (x < this.length()) {
            appended.string[x] = this.string[x];
            x = x + 1;
        }
        x = 0;
        while (x < s.length()) {
            appended.string[x + this.length()] = s.string[x];
            x = x + 1;
        }
        return appended;
    }

    public char charAt(int x) {
        return this.string[x];
    }
}

class PrintStream {
    public static void println(char c) {
        println(Char.toString(c));
    }
    public static void print(char c) {
        print(Char.toString(c));
    }
    public static void println(boolean b) {
        println(Boolean.toString(b));
    }
    public static void print(boolean b) {
        print(Boolean.toString(b));
    }
    public static void println(int i) {
        println(Integer.toString(i));
    }
    public static void print(int i) {
        print(Integer.toString(i));
    }
    public static void println(Object o) {
        print(o.toString());
        print("\n");
    }
    public static void print(Object o) {
        String s;
        s = o.toString();
        int x;
        x = 0;
        while (x < s.length()) {
            MJC.putc(s.charAt(x));
            x = x + 1;
        }
    }
}

class Boolean implements Comparable {
    public static String toString(boolean b) {
        if (b) {
            return "true";
        } else {
            return "false";
        }
    }
    private boolean b;
    public Boolean(boolean b) {
        this.b = b;
    }
    public String toString() {
        return Boolean.toString(b);
    }
    public int compareToKey() {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }
    public int compareTo(Comparable o) {
        return this.compareToKey() - o.compareToKey();
    }
}

class Integer implements Comparable {
    public static int MAX_VALUE =  2147483647;
    public static int MIN_VALUE = -2147483648;
    public static int SIZE = 32;
    private int i;
    public Integer(int i) {
        this.i = i;
    }
    public int compareTo(Comparable o) {
        return compareToKey() - o.compareToKey();
    }
    public int compareToKey() {
        return this.i;
    }
    public static Integer[] boxInt(int[] ary) {
        int i = 0;
        Integer[] ints = new Integer[ary.length];
        while (i < ary.length) {
            ints[i] = new Integer(ary[i]);
            i = i + 1;
        }
        return ints;
    }
    public static char digitToChar(int x) {
        String digits = "0123456789";
        if (x < digits.length() && x > -1) {
            return digits.charAt(x);
        } else {
            Exception.throw("Digit not between 0-9");
        }
        return 'X';
    }
    public String toString() {
        return Integer.toString(i);
    }
    public static String toString(int convert) {
        int size = 0;
        int num = convert;
        boolean neg = false;
        if (convert == Integer.MIN_VALUE) {
            /* for now, a special case is easiest, we can't represent -MIN_VALUE */
            return "-2147483648";
        }
        if (num < 0) {
            neg = true;
            num = 0 - num;
        }
        int pos = num;
        while (num > 0) {
            num = num / 10;
            size = size + 1;
        }
        /* minimum digit size should be at least 1 (for a zero) */
        if (size < 1) {
            size = 1;
        }
        if (neg) {
            size = size + 1;
        }
        char [] number = new char[size];

        int x = size - 1;
        num = pos;
        while (x > 0 || x == 0) {
            int digit = num % 10;
            num = num / 10;
            char c_digit = Integer.digitToChar(digit);
            number[x] = c_digit;
            x = x - 1;
        }
        if (neg) {
            number[0] = '-';
        }
        return String.makeStringChar(number);
    }
}

class System {
    public static PrintStream out = new PrintStream();
}

