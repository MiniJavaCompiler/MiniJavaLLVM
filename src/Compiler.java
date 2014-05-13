// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

import compiler.*;
import lexer.*;
import syntax.*;
import checker.*;
import codegen.*;
import interp.*;

import java.io.Reader;
import java.io.FileReader;
import java.io.FileNotFoundException;

import org.apache.commons.cli.*;

/** A top-level driver for the mini Java compiler.
 */
public class Compiler {
    /** A command line entry point to the mini Java compiler.
     */
    public static void main(String[] args) {
        CommandLineParser parser = new BasicParser();
        Options options = new Options();

        options.addOption("L", "LLVM", true, "LLVM Bitcode Output Location.");
        options.addOption("x", "x86", true, "x86 Output File Location");
        options.addOption("l", "llvm", false, "Dump Human Readable LLVM Code.");
        options.addOption("i", "input", true, "Compile to x86 Assembly.");
        options.addOption("I", "interp", false, "Run the interpreter.");
        options.addOption("h", "help", false, "Print this help message.");
        options.addOption("V", "version", false, "Version information.");

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("v")) {
                System.out.println("MiniJava Compiler");
                System.out.println("Written By Mark Jones http://web.cecs.pdx.edu/~mpj/");
                System.out.println("Extended for LLVM by Mitch Souders and Mark Smith");
            }
            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar mjc.jar -i <INPUT> (--x86 <OUT>|--LLVM <OUT>|--llvm)",
                                    options);
                System.exit(0);
            }
            int errors = 0;
            if (!cmd.hasOption("i")) {
                System.out.println("-i|--input requires a MiniJava input file");
                System.out.println("--help for more info.");
                errors++;
            }
            if (!cmd.hasOption("x") && !cmd.hasOption("l") && !cmd.hasOption("L")
                && !cmd.hasOption("I")) {
                System.out.println("--x86|--llvm|--LLVM|--interp required for some sort of output.");
                System.out.println("--help for more info.");
                errors++;
            }

            if (errors > 0) {
                System.exit(1);
            }
            String inputFile = cmd.getOptionValue("i");
            Handler handler = new SimpleHandler();
            try {
                Reader  reader = new FileReader(inputFile);
                compile(handler, reader, inputFile, cmd);
            } catch (FileNotFoundException e) {
                handler.report(new Failure("Cannot open input file " +
                                           inputFile));
            }
        } catch (ParseException exp) {
            System.out.println("Argument Error: " + exp.getMessage());
        }
    }

    /** A method for invoking the compiler without going through any
     *  particular user interface.
     */
    static void compile(Handler handler, Reader reader,
                        String inputFile, CommandLine cmd) {
        Source      source  = new JavaSource(handler, inputFile, reader);
        MjcLexer    lexer   = new MjcLexer(handler, source);
        syntax.Parser parser  = new syntax.Parser(handler, lexer);
        ClassType[] classes = parser.getClasses();
        if (new Context(handler, classes).check() != null) {
            if (cmd.hasOption("x")) {
                String assemblyFile = cmd.getOptionValue("x");
                Assembly assembly = Assembly.assembleToFile(assemblyFile);
                if (assembly == null) {
                    handler.report(new Failure("Cannot open file " +
                                               assemblyFile + " for output"));
                } else {
                    assembly.emitStart(inputFile);
                    for (int i = 0; i < classes.length; i++) {
                        classes[i].compile(assembly);
                    }
                    assembly.close();
                }
            }
            if (cmd.hasOption("L") || cmd.hasOption("l")) {
                LLVM llvm = new LLVM();
                llvm.llvmGen(classes,  cmd.getOptionValue("L"), cmd.hasOption("l"));
            }
            if (cmd.hasOption("I")) {
                MethEnv main = new Context(handler, classes).check();
                new State().call(main);
            }
        }
    }
}
