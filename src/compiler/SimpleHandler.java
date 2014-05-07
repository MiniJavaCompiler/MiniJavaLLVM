// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package compiler;

/** A simple implementation of the Handler interface that prints the
 *  position and description of each diagnostic on System.err, and
 *  then returns to the caller.
 */
public class SimpleHandler extends Handler {
    /** Respond to a diagnostic by displaying it on the error output
     *  stream.
     */
    protected void respondTo(Diagnostic d) {
        Position pos  = d.getPos();
        if (d instanceof Warning) {
            System.err.print("WARNING: ");
        } else {
            System.err.print("ERROR: ");
        }
        if (pos != null) {
            System.err.println(pos.describe());
        }
        String txt = d.getText();
        if (txt != null) {
            System.err.println(txt);
        }
        System.err.println();
    }
}
