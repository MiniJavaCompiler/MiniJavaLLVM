// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package compiler;

/** A base class for line-oriented source input.
 */
public abstract class Source extends Phase {
    /** Construct a new source input phase with a specified handler.
     */
    public Source(Handler handler) {
        super(handler);
    }

    /** Return a printable description of the source.
     */
    public abstract String describe();

    /** Read the next line from the input stream.
     *
     *  @return The next line, or null at the end of the input stream.
     */
    public abstract String readLine();

    /** Return the current line number.   By convention, we use 0 to
     *  indicate the absence of a line number; counting for regular
     *  lines begins at 1.
     */
    public abstract int getLineNo();

    /** Return the text of a specific line, if it is available.  If the
     *  requested source line is not available, then a null is returned.
     *  This mechanism is useful with sources that cache either the text
     *  of each line that has been read, or perhaps just the offsets of
     *  each line within a given file.  A console input source may use a
     *  queue like structure to provide history for the most recently
     *  entered line.  A source that uses an array of Strings as its input,
     *  or perhaps accesses the text from a text editor's buffer in an IDE,
     *  might be able to offer up lines through this method that have not
     *  yet been read using readLine().  On the other hand, some sources
     *  might choose to return null for every call to getLine(), which is
     *  the default behavior specified below, or to flush any caches of
     *  line data after the source has been closed.
     */
    public String getLine(int lineNo) {
        return null;
    }

    /** Close the input stream and any associated resources.  The default
     *  for this method is to do nothing.
     */
    public void close() {
        // Do nothing!
    }
}
