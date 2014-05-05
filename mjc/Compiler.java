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
        if (args.length != 2) {
            System.err.println("usage: java -jar mjc.jar --x86|--LLVM inputFile outputFile");
            return;
        }
        String type = args[0];
        String  inputFile  = args[1];
        String  outputFile = args[2];
        Handler handler    = new SimpleHandler();
        try {
            Reader  reader     = new FileReader(inputFile);
            compile(type, handler, reader, inputFile, outputFile);
        } catch (FileNotFoundException e) {
            handler.report(new Failure("Cannot open input file " +
                                       inputFile));
        }
    }

    /** A method for invoking the compiler without going through any
     *  particular user interface.
     */
    static void compile(String type, Handler handler, Reader reader,
                        String inputFile, String assemblyFile) {
        Source      source  = new JavaSource(handler, inputFile, reader);
        MjcLexer    lexer   = new MjcLexer(handler, source);
        Parser      parser  = new Parser(handler, lexer);
        ClassType[] classes = parser.getClasses();
        if (new Context(handler, classes).check() != null) {
            if (type.equals("--x86")) {
                Assembly assembly = Assembly.assembleToFile(assemblyFile);
                if (assembly == null) {
                    handler.report(new Failure("Cannot open file " +
                                               assemblyFile + " for output"));
                } else {
                    assembly.emitStart(inputFile);
                    for (int i = 0; i < classes.length; i++) {
                        classes[i].compile(assembly);
                    }
                }
            } else if (type.equals("--LLVM")) {
                LLVM llvm = new LLVM();
                llvm.emit(inputFile);
            }
        }
    }
}
