class Main {
    static void main() {
        int [][] test2 = new int[][] {
            new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}
        };
        int x = 0;
        while (x < test2.length) {
            int y = 0;
            while (y < test2[x].length) {
                System.out.print(x);
                System.out.print(" ");
                System.out.print(y);
                System.out.print(": ");
                System.out.print(test2[x][y]);
                System.out.print(", ");
                y = y + 1;
            }
            System.out.println("");
            x = x + 1;
        }
    }
}

