package deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Double Linked two sentinel List-based Deque
 * @param <T> a datatype to be input
 */
public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    public class TNode {
        private TNode next;
        private TNode prev;
        private T item;

        public TNode(T i, TNode nex, TNode pre) {
            next = nex;
            prev = pre;
            item = i;
        }
    }
    /** circular sentinel topology */
    private TNode sentinel;
    private int size;

    public LinkedListDeque() {
        size = 0;
        // sentinel point at itself
        sentinel = new TNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /** Adds an item of T to the front of the deque */
    @Override
    public void addFirst(T item) {
        size += 1;
        // No matter whether size is 0
        TNode newNode = new TNode(item, null, null);
        // access sentinel.next(first item) to connect first
        sentinel.next.prev = newNode;
        newNode.next = sentinel.next;
        //then connect with sentinel
        sentinel.next = newNode;
        newNode.prev = sentinel;
    }

    /** Adds an item of T to the back of the deque. */
    @Override
    public void addLast(T item) {
        size += 1;

        TNode newNode = new TNode(item, null, null);
        // access sentinel.prev(the last item) first
        sentinel.prev.next = newNode;
        newNode.prev = sentinel.prev;
        // then connect with sentinel
        sentinel.prev = newNode;
        newNode.next = sentinel;

    }


    /** Returns the number of items in the deque. */
    @Override
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last,
     * separated by a space. Once all the items have been printed, print out a new line.*/
    @Override
    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        TNode p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    /**  Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        T delete = sentinel.next.item;
        // access the second item through sentinel.next.next
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;

        return delete;
    }


    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;

        T delete = sentinel.prev.item;
        // same as above
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        return delete;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item,
     *  and so forth. If no such item exists, returns null. Must not alter the deque! */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        TNode p = sentinel.next;
        int i = 0;
        while (p != sentinel) {
            if (i == index) {
                break;
            }
            p = p.next;
            i += 1;
        }
        return p.item;
    }
    /** same function as get(), but use recursion */
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(TNode current, int index) {
        if (index == 0) {
            return current.item;
        } else {
            return getRecursiveHelper(current.next, index - 1);
        }
    }

    /**
     *  Returns whether or not the parameter o is equal to the Deque.
     *  o is considered equal if it is a Deque and if it contains the same contents
     *  (as goverened by the generic T’s equals method) in the same order.
     */
    public boolean equals(Object o) {
        boolean equals = o instanceof LinkedListDeque;
        if (!equals) {
            return false;
        }
        if (size != ((LinkedListDeque) o).size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            equals = get(i).equals(((LinkedListDeque) o).get(i));
            if (!equals) {
                return false;
            }
        }
        return equals;
    }

    /* Throw "The deque is empty if size == 0"*/
    public Iterator<T> iterator() {
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty");
        }
        return new LLDequeIterator();
    }

    private class LLDequeIterator implements Iterator<T> {
        private TNode p;

        public LLDequeIterator() {
            p = sentinel.next;
        }

        public boolean hasNext() {
            return p != sentinel;
        }

        public T next() {
            T item = p.item;
            p = p.next;
            return item;
        }

    }
}
