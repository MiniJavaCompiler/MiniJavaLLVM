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
import java.util.ArrayList;
import java.util.Arrays;

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
            compile(inputFile, cmd);
        } catch (ParseException exp) {
            System.out.println("Argument Error: " + exp.getMessage());
        }
    }

    /** A method for invoking the compiler without going through any
     *  particular user interface.
     */
    static void compile(String inputFile, CommandLine cmd) {
        String [] input_files = {inputFile, "src/Runtime.j"};
        Source fake = new JavaSource(null, "<MJCInternal>", null);
        Position fake_pos = new SourcePosition(fake, 0, 0);
        Handler handler = new SimpleHandler();
        Context context = new Context(fake_pos, handler);

        for (String i : input_files) {
            try {
                Reader  reader = new FileReader(i);
                Source      source  = new JavaSource(handler, i, reader);
                MjcLexer    lexer   = new MjcLexer(handler, source);
                syntax.Parser parser  = new syntax.Parser(handler, lexer);
                parser.setContext(context);
                parser.parseNow();
            } catch (FileNotFoundException e) {
                handler.report(new Failure("Cannot open input file " +
                                           i));
            }
        }
        MethEnv main = null;
        if ((main = context.check()) != null) {
            ClassType [] classes = context.getClasses();
            if (cmd.hasOption("x")) {
                String assemblyFile = cmd.getOptionValue("x");
                Assembly assembly = Assembly.assembleToFile(assemblyFile);
                if (assembly == null) {
                    handler.report(new Failure("Cannot open file " +
                                               assemblyFile + " for output"));
                } else {
                    assembly.emitStart(inputFile, classes, context.getUniqueStrings());
                    for (int i = 0; i < classes.length; i++) {
                        classes[i].compile(assembly);
                    }
                    assembly.close();
                }
            }
            if (cmd.hasOption("L") || cmd.hasOption("l")) {
                LLVM llvm = new LLVM();
                llvm.llvmGen(classes, context.getUniqueStrings(), cmd.getOptionValue("L"),
                             cmd.hasOption("l"));
            }
            if (cmd.hasOption("I")) {
                State s = new State();
                MethEnv init = context.findClass("MJCStatic").findMethod("init");
                s.call(init);
                s.call(main);
            }
        }
    }
}
