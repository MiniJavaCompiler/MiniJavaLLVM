
class System {
    static void out(int x);
}

class Main {
    static void main() {
        int i;
        i = 0;
        do {           
            System.out(i);
            i = i + 1;
        } while (i < 10);
    }
}

