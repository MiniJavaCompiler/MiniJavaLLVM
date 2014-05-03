// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package checker;

import compiler.*;
import lexer.*;
import syntax.*;
import checker.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/** A top-level driver for the mini Java checker.
 */
public class Checker {

    /** A command line entry point to the mini Java checker.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("usage: java mjc.Compiler inputFile");
            return;
        }
        String  inputFile = args[0];
        Handler handler   = new SimpleHandler();
        try {
            Reader reader = new FileReader(inputFile);
            check(handler, reader, inputFile);
        } catch (FileNotFoundException e) {
            handler.report(new Failure("Cannot open input file " +
                                       inputFile));
        }
    }

    /** A method for invoking the compiler without going through any
     *  particular user interface.
     */
    static void check(Handler handler, Reader reader, String inputFile) {
        Source      source  = new JavaSource(handler, inputFile, reader);
        MjcLexer    lexer   = new MjcLexer(handler, source);
        Parser      parser  = new Parser(handler, lexer);
        ClassType[] classes = parser.getClasses();
        if (new Context(handler, classes).check() != null) {
            System.out.println(
                "No static errors found in \"" + inputFile + "\"");
        }
    }
}
