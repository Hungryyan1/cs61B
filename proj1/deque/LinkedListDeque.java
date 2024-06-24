package deque;

/**
 * Double Linked two sentinel List-based Deque
 * @param <Dtype> a datatype to be input
 */
public class LinkedListDeque<Dtype> {
    public class DtypeNode {
        public DtypeNode next;
        public DtypeNode prev;
        public Dtype item;

        public DtypeNode(Dtype i, DtypeNode nex, DtypeNode pre) {
            next = nex;
            prev = pre;
            item = i;
        }
    }
    /** circular sentinel topology */
    private DtypeNode sentinel;
    private int size;

    public LinkedListDeque() {
        size = 0;
        // sentinel point at itself
        sentinel = new DtypeNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /** Adds an item of Dtype to the front of the deque */
    public void addFirst(Dtype item) {
        size += 1;
        // No matter whether size is 0
        DtypeNode newNode = new DtypeNode(item, null, null);
        // access sentinel.next(first item) to connect first
        sentinel.next.prev = newNode;
        newNode.next = sentinel.next;
        //then connect with sentinel
        sentinel.next = newNode;
        newNode.prev = sentinel;
    }

    /** Adds an item of Dtype to the back of the deque. */
    public void addLast(Dtype item) {
        size += 1;

        DtypeNode newNode = new DtypeNode(item, null, null);
        // access sentinel.prev(the last item) first
        sentinel.prev.next = newNode;
        newNode.prev = sentinel.prev;
        // then connect with sentinel
        sentinel.prev = newNode;
        newNode.next = sentinel;

    }


    /** Returns true if deque is empty, false otherwise */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last,
     * separated by a space. Once all the items have been printed, print out a new line.*/
    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        DtypeNode p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    /**  Removes and returns the item at the front of the deque. If no such item exists, returns null. */
    public Dtype removeFirst() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        Dtype delete = sentinel.next.item;
        // access the second item through sentinel.next.next
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;

        return delete;
    }


    /** Removes and returns the item at the back of the deque. If no such item exists, returns null. */
    public Dtype removeLast() {
        if (isEmpty()) {
            return null;
        }
        size -= 1;

        Dtype delete = sentinel.prev.item;
        // same as above
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        return delete;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item,
     *  and so forth. If no such item exists, returns null. Must not alter the deque! */
    public Dtype get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        DtypeNode p = sentinel.next;
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
    public Dtype getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }

    private Dtype getRecursiveHelper(DtypeNode current, int index) {
        if (index == 0) {
            return current.item;
        } else {
            return getRecursiveHelper(current.next, index - 1);
        }
    }
    //public Iterator<Dtype> iterator()

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
}
