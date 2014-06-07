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

/** A framework for building lexical analyzers.
 *
 *  Conventions: when a token has been recognized, save the integer
 *  code for it in the variable `token' and any corresponding text,
 *  if required, in the variable `textToken'.
 */
public abstract class Lexer extends Phase {
    protected int    token;
    protected String lexemeText;

    /** Construct a lexical analysis phase with a specified diagnostic
     *  handler.
     */
    public Lexer(Handler handler) {
        super(handler);
    }

    /** Construct a lexical analysis phase, and specify which token
     *  should be returned first.  This feature is useful in programs
     *  where an initial token is used to allow the same parser to be
     *  used, in effect, with multiple start symbols.  By loading the
     *  firstToken with particular values, we can force the parser to
     *  use different productions for the start symbol.
     *
     *  For example, in a program that needs to parse exprs, stmts,
     *  or files, all using elements from the same grammar, then we
     *  might set up a start symbol with the following productions:
     *  <pre>
     *    start : PARSE_EXPR expr
     *          | PARSE_STMT stmt
     *          | PARSE_FILE file
     *          ;
     *  </pre>
     *  To parse an expression, statement, or file, we just make sure
     *  that our lexer is constructed using the appropriate firstToken:
     *  <pre>
     *    new Lexer(handler, PARSE_EXPR);
     *    new Lexer(handler, PARSE_STMT);
     *    new Lexer(handler, PARSE_FILE);
     *  </pre>
     *  We do not specify either a text or a position for this first
     *  token, so a program using a Lexer should not attempt to read
     *  or use those attributes of the first token.
     *
     *  In an application where this feature is not required, the program
     *  should just use:
     *  <pre>
     *    new Lexer(handler);
     *  </pre>
     *  and then invoke <code>nextToken()</code> to read the first input
     *  token.
     */
    public Lexer(Handler handler, int firstToken) {
        super(handler);
        token = firstToken;
    }

    /** Read the next token and return the corresponding integer code.
     *  At the same time, the function should set lexemeText to the text
     *  of the lexeme (if relevant), and should be prepared to respond
     *  to a getPos request with the position at which the token began.
     *  This function should not normally be called again after an end
     *  of input token has been returned.
     */
    public abstract int nextToken();

    /** Returns the code for the current token.
     */
    public int getToken() {
        return token;
    }

    /** Returns the text (if any) for the current lexeme.
     */
    public String getLexeme() {
        return lexemeText;
    }

    /** Return a position describing where the current token was
     *  found.  This method is abstract because different types
     *  of lexer will need to return different types of source
     *  position depending on how they get their input.
     */
    public abstract Position getPos();

    /** Test for a particular next token code.
     */
    public boolean match(int token) {
        if (token == this.token) {
            nextToken();
            return true;
        }
        return false;
    }

    /** Close this lexer.  Calling this method signals that this
     *  lexer will not be used again, and allows the implementation
     *  to free up any resources that it might be holding such as
     *  files, input streams, etc.
     */
    public abstract void close();
}
