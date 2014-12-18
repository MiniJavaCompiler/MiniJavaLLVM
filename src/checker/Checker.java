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
        Source fake = new JavaSource(null, "<MJCInternal>", null);
        ClassType [] classes = new ClassType[] {};
        Position fake_pos = new SourcePosition(fake, 0, 0);
        Context context = new Context(fake_pos, handler);
        Source      source  = new JavaSource(handler, inputFile, reader);
        MjcLexer    lexer   = new MjcLexer(handler, source);
        Parser      parser  = new Parser(handler, lexer);
        parser.setContext(context);
        parser.parseNow();
        if (context.check() != null) {
            System.out.println(
                "No static errors found in \"" + inputFile + "\"");
        }
    }
}
