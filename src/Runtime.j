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

class Class {
    private int [] instancesof;
    private String name;
    private int classRefId;
    public Class(String name, int classRefId, int [] instancesof) {
        this.name = name;
        this.instancesof = instancesof;
        this.classRefId = classRefId;
    }
    public String getName() {
        return name;
    }
    public boolean isInstance(Object obj) {
        int x;
        if (classRefId == obj.classId) {
            return true;
        }
        x = 0;
        while (x < instancesof.length) {
            if (instancesof[x] == obj.classId) {
                return true;
            }
            x = x + 1;
        }
        return false;
    }
    public int getClassRefId() {
        return classRefId;
    }
}

class ClassPool {
    private static ClassPool classPool = null;
    private Class [] pool;
    private int size;
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
    public void addClass(Class c) {
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
        pool[used] = c;
        used = used + 1;
    }
    public Class findClass(int classId) {
        int x = 0;
        while (x < used) {
            if (pool[x].getClassRefId() == classId) {
                return pool[x];
            }
            x = x + 1;
        }
        return null;
    }
    public void printDebug() {
        int x = 0;
        System.out.println("Size");
        System.out.println(Integer.toString(pool.length));

        System.out.println("Used");
        System.out.println(Integer.toString(used));

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
    public static void println(String o) {
        print(o);
        print("\n");
    }
    public static void print(String o) {
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

class Boolean {
    public static String toString(boolean b) {
        if (b) {
            return "true";
        } else {
            return "false";
        }
    }
}

class Integer {
    public static char digitToChar(int x) {
        String digits = "0123456789";
        if (x < digits.length()) {
            return digits.charAt(x);
        } else {
            Exception.throw("Digit not between 0-9");
        }
        return 'X';
    }
    public static String toString(int convert) {
        int size = 0;
        int num = convert;
        boolean neg = false;
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

