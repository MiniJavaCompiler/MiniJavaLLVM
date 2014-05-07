// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package syntax;

/** A public class that allows us to access the definitions of the
 *  Mjc tokens from outside the syntax package; this works around a
 *  design decision in jacc that restricts the generated token file
 *  to package level access only.
 */
public interface Tokens extends MjcTokens {
}
