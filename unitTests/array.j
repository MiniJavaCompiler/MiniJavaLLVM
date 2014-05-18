class Main {
    static void main() {
        MJC.putc('x');        
        System.out.println("Test");
        
        char [] test = new char[3];
        test[0] = 'p';        
        
        test[1] = 'q'; 
        test[2] = '\n';
        String s = String.makeStringChar(test);
        System.out.print(s);
        String big = "test123";
        String big2 = "something else";
        String biggest = big.append(big2);
        System.out.println(biggest);

        System.out.println(Integer.toString(1742));
        System.out.println(Integer.toString(-253));
    }
}
