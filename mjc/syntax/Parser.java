// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

import compiler.Handler;
import lexer.MjcLexer;

/** Provides public access to the MjcParser.
 */
public class Parser extends MjcParser {
  public Parser(Handler handler, MjcLexer lexer) {
    super(handler, lexer);
  }
}
