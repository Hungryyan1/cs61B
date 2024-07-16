package deque;

import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item> {
    private Comparator<Item> comparator;

    public MaxArrayDeque(Comparator<Item> c) {
        comparator = c;
    }

    /*
     returns the maximum element in the deque as governed by the previously given Comparator.
      If the MaxArrayDeque is empty, simply return null.
     */
    public Item max() {
        if (isEmpty()) {
            return null;
        }
        Item max = get(0);
        for (int i = 0; i < size(); i++) {
            if (comparator.compare(get(i), max) > 0) {
                max = get(i);
            }
        }
        return max;
    }

    /*
    returns the maximum element in the deque
    as governed by the parameter Comparator c.
    If the MaxArrayDeque is empty, simply return null.
     */
    public Item max(Comparator<Item> c) {
        if (isEmpty()) {
            return null;
        }
        Item max = get(0);
        for (int i = 0; i < size(); i++) {
            if (c.compare(get(i), max) > 0) {
                max = get(i);
            }
        }
        return max;
    }
}
