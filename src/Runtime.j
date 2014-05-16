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
    public static char digitToChar(int x) {
        if (x == 0) {
            return '0';
        } else if (x == 1) {
            return '1';
        } else if (x == 2) {
            return '2';
        } else if (x == 3) {
            return '3';
        } else if (x == 4) {
            return '4';
        } else if (x == 5) {
            return '5';
        } else if (x == 6) {
            return '6';
        } else if (x == 7) {
            return '7';
        } else if (x == 8) {
            return '8';
        } else if (x == 9) {
            return '9';
        } else {
            System.out.println("Digit not between 0-9");
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

