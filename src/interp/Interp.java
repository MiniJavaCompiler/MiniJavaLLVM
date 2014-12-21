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
        Source fake = new JavaSource(null, "<MJCInternal>", null);
        Position fake_pos = new SourcePosition(fake, 0, 0);
        Parser      parser  = new Parser(handler, lexer);
        Context     context = new Context(fake_pos, handler);
        parser.setContext(context);
        parser.parseNow();
        MethEnv     main    = context.check();
        MethEnv init = context.findClass("MJCStatic").findMethod("init", null);
        if (main != null) {
            State s = new State();
            s.call(init);
            s.call(main);
        }
    }

    static public void abort(String msg) {
        System.err.println(msg);
        System.exit(1); // bad style, but this will do for now :-)
    }
}

