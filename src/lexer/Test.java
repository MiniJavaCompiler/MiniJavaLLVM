/*
 * MiniJava Compiler - X86, LLVM Compiler/Interpreter for MiniJava.
 * Copyright (C) 2014, 2008 Mitch Souders, Mark A. Smith, Mark P. Jones
 *
 * MiniJava Compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * MiniJava Compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MiniJava Compiler; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


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
