class Main {
    static void main() {
        char [] test = new char[] {'a', 'b', 'c'};
        String s = String.makeStringChar(test);
        System.out.println(s);

        int [] test2 = new int[] {1, 2, 3, 4, 5, 6};
        int x = 0;
        while (x < test2.length) {
            System.out.println(test2[x]);
            x = x + 1;
        }
    }
}
