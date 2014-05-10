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
