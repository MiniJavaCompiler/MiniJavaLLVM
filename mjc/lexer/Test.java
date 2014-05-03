// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package lexer;

import java.io.Reader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import compiler.*;
import syntax.Tokens;

/** A program to test the mini Java lexer.
 */
public class Test implements Tokens {
    /** A command line entry point.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("usage: java lexer.Test inputFile");
            return;
        }
        String  inputFile  = args[0];
        Handler handler    = new SimpleHandler();
        try {
            Reader   reader = new FileReader(inputFile);
            // Use these two lines for Handwritten Lexer:
            Source      source = new JavaSource(handler, inputFile, reader);
            MjcLexer lexer  = new MjcLexer(handler, source);
            // Use this line for Generated Lexer:
            //Generated lexer  = new Generated(reader);

            int tok = lexer.nextToken();
            while (tok != ENDINPUT) {
                System.out.println(displayToken(tok, lexer.getSemantic()));
                tok = lexer.nextToken();
            }
            System.out.println("ENDINPUT");

        } catch (FileNotFoundException e) {
            handler.report(new Failure("Cannot open input file " +
                                       inputFile));
        }
    }

    private static String displayToken(int tok, Object semantic) {
        switch (tok) {
        case BOOLEAN :
            return "BOOLEAN";
        case CAND    :
            return "CAND";
        case CLASS   :
            return "CLASS";
        case COR     :
            return "COR";
        case ELSE    :
            return "ELSE";
        case EQEQ    :
            return "EQEQ";
        case EXTENDS :
            return "EXTENDS";
        case FALSE   :
            return "FALSE";
        case IDENT   :
            return "IDENT(" + semantic + ")";
        case IF      :
            return "IF";
        case INT     :
            return "INT";
        case INTLIT  :
            return "INTLIT(" + semantic + ")";
        case NEQ     :
            return "NEQ";
        case NEW     :
            return "NEW";
        case NULL    :
            return "NULL";
        case RETURN  :
            return "RETURN";
        case STATIC  :
            return "STATIC";
        case SUPER   :
            return "SUPER";
        case THIS    :
            return "THIS";
        case TRUE    :
            return "TRUE";
        case VOID    :
            return "VOID";
        case WHILE   :
            return "WHILE";
        case '!'     :
            return "\"!\"";
        case '&'     :
            return "\"&\"";
        case '('     :
            return "\"(\"";
        case ')'     :
            return "\")\"";
        case '*'     :
            return "\"*\"";
        case '+'     :
            return "\"+\"";
        case ','     :
            return "\",\"";
        case '-'     :
            return "\"-\"";
        case '.'     :
            return "\".\"";
        case '/'     :
            return "\"/\"";
        case ';'     :
            return "\";\"";
        case '<'     :
            return "\"<\"";
        case '='     :
            return "\"=\"";
        case '>'     :
            return "\">\"";
        case '^'     :
            return "\"^\"";
        case '{'     :
            return "\"{\"";
        case '|'     :
            return "\"|\"";
        case '}'     :
            return "\"}\"";
        default      :
            return "UNKNOWN TOKEN";
        }
    }
}
