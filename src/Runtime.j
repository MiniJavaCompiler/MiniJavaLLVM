class MJC {
    static void print(char s);
}

/* If you modify Object or String and add new fields, MJC
   will not properly create new string literal objects */
class Object {
    public String toString() { 
        return "<SomeObject>";
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
        while (x < c.length) {
            s.string[x] = c[x];           
            x = x + 1;
        }
        return s;
    }
    public String toString() {
        return this;
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
    public static void println(Object o) {
        print(o);
        print("\n");
    }
    public static void print(Object o) {
        String s;
        s = o.toString();
        int x;
        x = 0;
        while (x < s.length()) {
            MJC.print(s.charAt(x));
            x = x + 1;
        }
    }
}

class Integer {
    public static String toString(int x) {
        return "";
    }
}

class System {
    public static PrintStream out; //= new PrintStream();
}

