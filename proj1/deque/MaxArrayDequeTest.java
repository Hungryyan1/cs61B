package deque;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {

    private static class IntComparator implements Comparator<Integer> {
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public static Comparator<Integer> getIntComparator() {
        return new IntComparator();
    }

    private static class IntComparatorInverse implements Comparator<Integer> {
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    }

    public static Comparator<Integer> getIntComparatorInverse() {
        return new IntComparatorInverse();
    }

    private static class StringComparator implements Comparator<String> {
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    public static Comparator<String> getStringComparator() {
        return new StringComparator();
    }

    @Test
    public void TestForInt() {
        Comparator<Integer> comparator = getIntComparator();
        MaxArrayDeque<Integer> ad1 = new MaxArrayDeque<>(comparator);
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addFirst(3);

        assertEquals((int) ad1.max(), 3);
    }

    @Test
    public void TestForString() {
        Comparator<String> comparator = getStringComparator();
        MaxArrayDeque<String> ad1 = new MaxArrayDeque<>(comparator);
        ad1.addFirst("Hungry");
        ad1.addFirst("Eating");
        ad1.addFirst("Starving");

        assertSame(ad1.max(), "Starving");
    }

    @Test
    public void TestForChangeParameter() {
        Comparator<Integer> comparator = getIntComparator();
        Comparator<Integer> comparatorInverse = getIntComparatorInverse();
        MaxArrayDeque<Integer> ad1 = new MaxArrayDeque<>(comparatorInverse);

        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addFirst(3);

        assertEquals((int) ad1.max(), 1);
        assertEquals((int) ad1.max(comparator), 3);
    }
}
