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

package util;
import java.util.Iterator;

public class ListIterator<T extends ListIteratorIF<T>> implements Iterator<T> {
    private T next;
    private T curr;
    public ListIterator(T head) {
        this.next = head;
        this.curr = null;
    }
    public boolean hasNext() {
        return this.next != null;
    }
    public T next() {
        this.curr = this.next;
        this.next = this.curr.getNext();
        return this.curr;
    }

    public void remove() {
        throw new RuntimeException("Iterator does not allow removal.");
    }
}
