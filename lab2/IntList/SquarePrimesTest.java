package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    /**
     * not sure if we should consider empty lists
     */
    //@Test
    //public void testSquarePrimesEmptyList() {
    //    IntList lst = IntList.of();
    //    boolean changed = IntListExercises.squarePrimes(lst);
    //    assertEquals(" ", lst.toString());
    //    assertFalse(changed);
    //}

    /**
     * Test the case that only exist one prime
     */
    @Test
    public void testSquarePrimesOnePrime() {
        IntList lst = IntList.of(2);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4", lst.toString());
        assertTrue(changed);
    }

    /**
     * Test the case that only exist on non-prime
     */
    @Test
    public void testSquarePrimesOneNonPrime() {
        IntList lst = IntList.of(100);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("100", lst.toString());
        assertFalse(changed);
    }

    /**
     * Test prime in the front
     */
    @Test
    public void testSquarePrimesInTheFront() {
        IntList lst = IntList.of(19, 20, 10, 1000);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("361 -> 20 -> 10 -> 1000", lst.toString());
        assertTrue(changed);
    }

    /**
     * Test prime in the back
     */
    @Test
    public void testSquarePrimesInTheBack() {
        IntList lst = IntList.of(10, 20, 30, 40, 7);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("10 -> 20 -> 30 -> 40 -> 49", lst.toString());
        assertTrue(changed);
    }

    /**
     * Test all primes
     */
    @Test
    public void testSquarePrimesAllPrimes() {
        IntList lst = IntList.of(13, 7, 5, 17, 19);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("169 -> 49 -> 25 -> 289 -> 361", lst.toString());
        assertTrue(changed);
    }

    /**
     * Test no primes
     */
    @Test
    public void testSquarePrimesNoPrimes() {
        IntList lst = IntList.of(14, 15, 16, 20, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 20 -> 18", lst.toString());
        assertFalse(changed);
    }
}
