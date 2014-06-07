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


package compiler;

/** A source input phase that reads lines of input from standard input.
 */
public class StdinSource extends Source {
    /** Construct a standard input source with a specified
     *  diagnostic handler.
     */
    public StdinSource(Handler handler) {
        super(handler);
    }

    /** Return a description of this source.
     */
    public String describe() {
        return "standard input";
    }

    /** Flag indicates when the end of input has been found.
     */
    private boolean foundEOF = false;

    /** Counter records numbers of each line that is returned.
     */
    private int lineNumber = 0;

    /** A StringBuffer that is used to store input lines as they are
     *  read.
     */
    private StringBuffer buf = new StringBuffer();

    /** Read the next line from the input stream.
     *
     *  @return The next line, or null at the end of the input stream.
     */
    public String readLine() {
        if (foundEOF) {
            return null;
        }
        lineNumber++;
        buf.setLength(0);
        for (;;) {
            int c = 0;
            try {
                c = System.in.read();
            } catch (Exception e) {
                report(new Failure("Error in input stream"));
            }
            if (c == '\n') {
                break;
            } else if (c < 0) {
                foundEOF = true;
                break;
            }
            buf.append((char)c);
        }
        return buf.toString();
    }

    /** Return the current line number.
     */
    public int getLineNo() {
        return lineNumber;
    }
}
