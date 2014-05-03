// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package compiler;

/** Represents a warning diagnostic.
 */
public class Warning extends Diagnostic {
    /** Construct a simple warning with a fixed description.
     */
    public Warning(String text) {
        super(text);
    } 

    /** Construct a warning object for a particular source position.
     */
    public Warning(Position position) {
        super(position);
    } 

    /** Construct a simple warning with a fixed description
     *  and a source position.
     */
    public Warning(Position position, String text) {
        super(position, text);
    } 
}
