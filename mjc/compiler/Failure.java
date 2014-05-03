// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package compiler;

/** Represents an error diagnostic.  To avoid a clash with java.lang.Error,
 *  we resisted the temptation to call this class Error.
 */
public class Failure extends Diagnostic {
    /** Construct a simple failure report with a fixed description.
     */
    public Failure(String text) {
        super(text);
    } 

    /** Construct a failure report for a particular source position.
     */
    public Failure(Position position) {
        super(position);
    } 

    /** Construct a simple failure report with a fixed description
     *  and a source position.
     */
    public Failure(Position position, String text) {
        super(position, text);
    } 
}
