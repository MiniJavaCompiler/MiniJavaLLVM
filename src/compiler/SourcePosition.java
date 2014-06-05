// Copyright (c) Mark P Jones, Portland State University
// Subject to conditions of distribution and use; see LICENSE for details
// February 3 2008 11:12 AM

package compiler;

/** A position within a source Source object.
 *
 *  [We might also like to have a mechanism for starting an editor at a
 *  particular position so that users can view and potentially change the
 *  corresponding sections of source code.]
 */
public class SourcePosition extends Position {
    private Source source;
    private int    row;
    private int    column;

    public SourcePosition(Source source, int row, int column) {
        this.source = source;
        this.row    = row;
        this.column = column;
    }

    public SourcePosition(Source source) {
        this(source, 0, 0);
    }

    /** Return the source for this position.
     */
    public Source getSource() {
        return source;
    }

    /** Return the row number for this position.
     */
    public int getRow() {
        return row;
    }

    /** Return the column number for this position.
     */
    public int getColumn() {
        return column;
    }

    /** Update the coordinates of this position.
     */
    public void updateCoords(int row, int column) {
        this.row    = row;
        this.column = column;
    }

    public String getFilename() {
        return source.describe();
    }
    /** Obtain a printable description of the source position.
     */
    public String describe() {
        StringBuffer buf = new StringBuffer();
        if (source != null) {
            buf.append('"');
            buf.append(source.describe());
            buf.append('"');
            if (row > 0) {
                buf.append(", ");
            }
        }
        if (row > 0) {
            buf.append("line ");
            buf.append(row);
        }
        if (source != null) {
            String line = source.getLine(row);
            if (line != null) {
                buf.append('\n');
                buf.append(line);
                buf.append('\n');
                for (int i = 0; i < column; i++) {
                    buf.append(' ');
                }
                buf.append('^');
            }
        }
        return (buf.length() == 0) ? "input" : buf.toString();
    }

    /** Copy a source position.
     */
    public Position copy() {
        return new SourcePosition(source, row, column);
    }
}
