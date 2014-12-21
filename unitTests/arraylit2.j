class Main {
    static void main() {
        int [][] test2 = new int[][] {
            new int[]{1, 2, 3, 4, 5, 6},
            new int[]{1, 2, 3},
            new int[]{9, 2, 3, 4},
            new int[]{},
            new int[]{1, 2, 3, 4, 5, 6, 7, 8}
        };
        int x = 0;
        while (x < test2.length) {
            int y = 0;
            while (y < test2[x].length) {
                System.out.print(test2[x][y]);
                System.out.print(" ");
                y = y + 1;
            }
            System.out.println("");
            x = x + 1;
        }
    }
}
