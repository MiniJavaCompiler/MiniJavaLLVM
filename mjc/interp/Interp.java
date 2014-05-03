// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package interp;

import compiler.*;
import lexer.*;
import syntax.*;
import checker.*;

import java.io.Reader;
import java.io.FileReader;
import java.io.FileNotFoundException;

/** A top-level driver for the mini Java interpreter.
 */
public class Interp {
    /** A command line entry point to the mini Java interpreter.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("usage: java mjc.Interp inputFile");
            return;
        }
        String  inputFile  = args[0];
        Handler handler    = new SimpleHandler();
        try {
            Reader reader  = new FileReader(inputFile);
            interp(reader, inputFile, handler);
        } catch (FileNotFoundException e) {
            handler.report(new Failure("Cannot open input file " +
                                       inputFile));
        }
    }

    /** A method for invoking the interpreter without going through any
     *  particular user interface.
     */
    public static void interp(Reader reader, String description,
                              Handler handler) {
        Source      source  = new JavaSource(handler, description, reader);
        MjcLexer    lexer   = new MjcLexer(handler, source);
        Parser      parser  = new Parser(handler, lexer);
        ClassType[] classes = parser.getClasses();
        MethEnv     main    = new Context(handler, classes).check();
        if (main != null) {
            new State().call(main);
        }
    }

    static public void abort(String msg) {
        System.err.println(msg);
        System.exit(1); // bad style, but this will do for now :-)
    }
}

