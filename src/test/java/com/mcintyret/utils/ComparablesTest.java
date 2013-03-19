package com.mcintyret.utils;

import org.testng.annotations.Test;

import java.util.Comparator;

import static com.mcintyret.utils.Comparables.*;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */

@Test
public class ComparablesTest {

    private final int ONE = 1;
    private final int TWO = 2;
    
    public void shouldLessThan() {
        assertTrue(lessThan(ONE, TWO));
        assertFalse(lessThan(TWO, ONE));
        assertFalse(lessThan(ONE, ONE));
    }
    
    public void shouldLessThanOrEquals() {
        assertTrue(lessThanOrEqual(ONE, TWO));
        assertFalse(lessThanOrEqual(TWO, ONE));
        assertTrue(lessThanOrEqual(ONE, ONE));
    }
    
    public void shouldGreaterThan() {
        assertFalse(greaterThan(ONE, TWO));
        assertTrue(greaterThan(TWO, ONE));
        assertFalse(greaterThan(ONE, ONE));
    }
    
    public void shouldGreaterThanOrEquals() {
        assertFalse(greaterThanOrEqual(ONE, TWO));
        assertTrue(greaterThanOrEqual(TWO, ONE));
        assertTrue(greaterThanOrEqual(ONE, ONE));
    }

    public void shouldEqual() {
        assertFalse(equal(ONE, TWO));
        assertFalse(equal(TWO, ONE));
        assertTrue(equal(ONE, ONE));
    }

    public void shouldMakeComparableComparator() {
        Comparator<Integer> comp = comparableComparator();

        assertTrue(comp.compare(ONE, TWO) < 0);
        assertTrue(comp.compare(TWO, ONE) > 0);
        assertTrue(comp.compare(ONE, ONE) == 0);
    }

}
