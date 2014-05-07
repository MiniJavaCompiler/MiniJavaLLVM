// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

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
