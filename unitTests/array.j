class Main {
    static void main() {
        System.out = new PrintStream();
        System.out.println("Test");
        char [] test;
        test = new char[3];
        test[0] = 'p';
        test[1] = 'q'; 
        test[2] = '\n';        
        String s;
        s = String.makeStringChar(test);
        System.out.print(s);

        String big;
        big = "test123";
        String big2;
        big2 = "something else";
        String biggest;
        biggest = big.append(big2);
        System.out.println(biggest);
    }
}