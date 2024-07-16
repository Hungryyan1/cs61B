package deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<Item> implements Iterable<Item>, Deque<Item> {
    private int size;
    private Item[] items;
    private int nextfirst;
    private int nextlast;

    public ArrayDeque() {
        items = (Item[]) new Object[8];
        // start from middle
        nextfirst = 3;
        nextlast = 4;
    }
    @Override
    public int size() {
        return size;
    }


    @Override
    public void addFirst(Item item) {
        if (size + 1> items.length) {
            resize(items.length * 2);
        }
        size += 1;
        items[nextfirst] = item;
        if (nextfirst == 0) {
            nextfirst = items.length - 1;
        } else {
            nextfirst--;
        }
    }
    @Override
    public void addLast(Item item) {
        if (size + 1> items.length) {
            resize(items.length * 2);
        }
        size += 1;
        items[nextlast] = item;
        if (nextlast == items.length - 1) {
            nextlast = 0;
        } else {
            nextlast++;
        }
    }
    /* My strategy is to extend the array from both ends ,
    *  copy the old (sorted) to the middle of new array
    */
    private void resize(int newSize) {
        Item[] newItems = (Item[]) new Object[newSize];

        int start = (newSize - size) / 2;

        Item[] sortedItems = sortArrayFromFirstToLast();

        System.arraycopy(sortedItems, 0, newItems, start, size);

        items = newItems;

        nextfirst = start - 1;
        nextlast = start + size;
    }

    private Item[] sortArrayFromFirstToLast() {
        Item[] sortItems = (Item[]) new Object[size];
        int first = (nextfirst + 1) % items.length;  // first item index
        int last = (nextlast + items.length - 1 ) % items.length; // last item index
        if (first >= last) {
            for (int i = first; i < items.length; i++) {
                sortItems[i - first] = items[i];
            }
            for (int i = 0; i <= last; i++) {
                sortItems[items.length - first + i] = items[i];
            }
        } else {
            for (int i = first; i <= last; i++) {
                sortItems[i - first] = items[i];
            }
        }
        return sortItems;
    }
    @Override
    public void printDeque() {
        if (size == 0) {
            return;
        }
        Item[] sortItems = sortArrayFromFirstToLast();
        for (int i = 0; i < size; i++) {
            System.out.print(sortItems[i] + " ");
        }
        System.out.println();
    }
    @Override
    public Item removeFirst() {
        if (size == 0) {
            return null;
        }
        if (items.length >= 16 && (size / (items.length * 1.0) < 0.25)) {
            resize(items.length / 2);
        }
        size -= 1;
        int first = (nextfirst + 1) % items.length;
        Item item = items[first];
        items[first] = null;
        nextfirst = first;
        return item;
    }
    @Override
    public Item removeLast() {
        if (size == 0) {
            return null;
        }
        // before performing a remove operation that will bring
        // the number of elements in the array under 25% the
        // length of the array, you should resize the size of the array down
        if (items.length >= 16 && (size / (items.length * 1.0) < 0.25)) {
            resize(items.length / 2);
        }
        size -= 1;
        int last = (nextlast + items.length - 1 ) % items.length;
        Item item = items[last];
        items[last] = null;
        nextlast = last;
        return item;
    }
    @Override
    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        int first = (nextfirst + 1) % items.length;

        return items[(first + index) % items.length];

    }

    public boolean equals(Object o) {
        boolean equals = o instanceof ArrayDeque;
        if (!equals) {
            return false;
        }
        if (size != ((ArrayDeque) o).size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            equals = get(i).equals(((ArrayDeque) o).get(i));
            if (!equals) {
                return false;
            }
        }
        return equals;
    }

    /* Throw "The deque is empty if size is 0" */
    public Iterator<Item> iterator() {
        if (size == 0) {
            throw new NoSuchElementException("The deque is empty");
        }
        return new ArrayDequeIterator();
    }

    /* Iterate the Deque according to sequence */
    private class ArrayDequeIterator implements Iterator<Item> {
        private int index;
        private Item[] itemsSort;

        public ArrayDequeIterator() {
            index = 0;
            itemsSort = sortArrayFromFirstToLast();
        }

        public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            Item item = itemsSort[index];
            index += 1;
            return item;
        }
    }
}
