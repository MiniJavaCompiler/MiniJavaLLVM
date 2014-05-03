// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

import compiler.*;
import lexer.*;
import syntax.*;
import checker.*;
import codegen.*;

import java.io.Reader;
import java.io.FileReader;
import java.io.FileNotFoundException;

/** A top-level driver for the mini Java compiler.
 */
public class Compiler {
    /** A command line entry point to the mini Java compiler.
     */
    public static void main(String[] args) {
        if (args.length!=2) {
            System.err.println("usage: java -jar mjc.jar inputFile outputFile");
            return;
        }
        String  inputFile  = args[0];
        Handler handler    = new SimpleHandler();
        try {
            Reader  reader     = new FileReader(inputFile);
            String  outputFile = args[1];
            compile(handler, reader, inputFile, outputFile);
        } catch (FileNotFoundException e) {
            handler.report(new Failure("Cannot open input file " +
                                       inputFile));
        }
    }

    /** A method for invoking the compiler without going through any
     *  particular user interface.
     */
    static void compile(Handler handler, Reader reader,
                        String inputFile, String assemblyFile) {
        Source      source  = new JavaSource(handler, inputFile, reader);
        MjcLexer    lexer   = new MjcLexer(handler, source);
        Parser      parser  = new Parser(handler, lexer);
        ClassType[] classes = parser.getClasses();
        if (new Context(handler, classes).check()!=null) {
            Assembly assembly = Assembly.assembleToFile(assemblyFile);
            if (assembly==null) {
                handler.report(new Failure("Cannot open file " +
                                           assemblyFile + " for output"));
            } else {
                assembly.emitStart(inputFile);
                for (int i=0; i<classes.length; i++) {
                    classes[i].compile(assembly);
                }
                assembly.close();
            }
        }
    }
}
