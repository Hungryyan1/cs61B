package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {


        ArrayDeque<String> lld1 = new ArrayDeque<String>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        lld1.addLast("back2");
        lld1.addLast("back3");
        lld1.addLast("back4");
        lld1.addLast("back5");
        lld1.addLast("back6");
        assertEquals(8, lld1.size());

        lld1.addLast("back7");
        assertEquals(9, lld1.size());
        lld1.addFirst("FrontFront");
        assertEquals(10, lld1.size());
        System.out.println("Printing out deque: ");
        lld1.printDeque();

    }
    @Test
    public void printDeckTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addFirst(3);
        ad1.addFirst(4);
        ad1.addFirst(5);
        ad1.addFirst(6);
        ad1.addFirst(7);
        ad1.addFirst(8);
        System.out.println("Printing out deque: ");
        ad1.printDeque();

        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        ad2.addLast(1);
        ad2.addLast(2);
        ad2.addLast(3);
        ad2.addLast(4);
        ad2.addLast(5);
        ad2.addLast(6);
        ad2.addLast(7);
        ad2.addLast(8);
        ad2.addLast(9);
        ad2.addLast(10);
        ad2.addLast(11);
        ad2.addLast(12);
        ad2.addLast(13);
        ad2.addLast(14);
        ad2.addLast(15);
        ad2.addLast(16);
        ad2.addLast(17);
        System.out.println("Printing out deque: ");
        ad2.printDeque();

        ArrayDeque<Integer> ad3 = new ArrayDeque<>();
        ad3.addFirst(1);
        ad3.addFirst(2);
        ad3.addLast(3);
        ad3.addLast(4);
        ad3.addLast(5);
        ad3.addLast(6);
        ad3.addLast(7);
        ad3.addLast(8);
        System.out.println("Printing out deque: ");
        ad3.printDeque();
    }


    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards.*/
    public void addRemoveTest() {


        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeFirst();
        // should be empty
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }
    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {


        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }
    @Test
    /* Check if you can create ArrayDeque with different parameterized types */
    public void multipleParamTest() {


        ArrayDeque<String>  lld1 = new ArrayDeque<String>();
        ArrayDeque<Double>  lld2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

        assertEquals("string", s);
        assertTrue(b);

    }
    @Test
    /* check if null is return when removing from an empty ArrayDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /* check if add and remove work fine */
    public void AddRemoveTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);

        assertEquals(8,(int) ad1.removeLast());
        assertEquals(1,(int) ad1.removeFirst());
        assertEquals(7,(int) ad1.removeLast());
        assertEquals(2,(int) ad1.removeFirst());
        assertEquals(4,ad1.size());

        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();

        assertNull(ad1.removeFirst());
        assertNull(ad1.removeLast());


    }


    @Test
    /* Add large number of elements to deque; check if order is correct.*/
    public void bigLLDequeTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

    }

    @Test
    /* check if the get method work properly */
    public void addGetTest() {
        ArrayDeque<Integer> lld = new ArrayDeque<Integer>();
        // empty list should return null
        assertNull(lld.get(0));
        lld.addFirst(1);
        assertEquals(1, (int) lld.get(0));
        lld.addLast(2);
        assertEquals(2, (int) lld.get(1));

        assertNull(lld.get(2));

        lld.addFirst(3);
        lld.addLast(4);
        lld.addLast(5);
        lld.addLast(6);
        lld.addLast(7);
        lld.addLast(8);

        assertEquals(3, (int) lld.get(0));
        assertEquals(1, (int) lld.get(1));
        assertEquals(2, (int) lld.get(2));
        assertEquals(4, (int) lld.get(3));
        assertEquals(5, (int) lld.get(4));
        assertEquals(6, (int) lld.get(5));
        assertEquals(7, (int) lld.get(6));
        assertEquals(8, (int) lld.get(7));
    }

    //@Test
    /* check if the getRecursive method work properly
    public void addGetRecursiveTest() {
        ArrayDeque<Integer> lld = new ArrayDeque<Integer>();
        // empty list should return null
        assertNull(lld.getRecursive(0));
        lld.addFirst(1);
        assertEquals(1, (int) lld.getRecursive(0));
        lld.addLast(2);
        assertEquals(2, (int) lld.getRecursive(1));

        assertNull(lld.getRecursive(2));
    }
    */

    @Test
    /* Test for equals */
    public void equalsTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        ArrayDeque<Integer> lld2 = new ArrayDeque<>();
        assertTrue(lld1.equals(lld2));

        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld2.addLast(1);
        lld2.addLast(2);
        lld2.addLast(3);

        assertTrue(lld1.equals(lld2));

        lld2.removeFirst();
        System.out.println(lld2.size());
        assertFalse(lld1.equals(lld2));


    }
}
