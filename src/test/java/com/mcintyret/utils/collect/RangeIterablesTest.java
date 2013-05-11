package com.mcintyret.utils.collect;

import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */

@Test
public class RangeIterablesTest {

    public void shouldMakeStringRange() {
        assertEqual(RangeIterables.ofRange("bar", "baz"),
                "bar", "bas", "bat", "bau", "bav", "baw", "bax", "bay", "baz");
    }

    public void shouldMakeStringRangeToLimit() {
        List<String> list = Lists.newArrayList(RangeIterables.ofRange("aa", "zz"));

        assertEquals(26 * 26, list.size());
        assertEquals("aa", list.get(0));
        assertEquals("zz", list.get(list.size() - 1));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailForStringRangeOfDifferentLengths() {
        RangeIterables.ofRange("left", "right");
    }

    public void shouldMakeIntegerRange() {
        assertEqual(RangeIterables.ofRange(-5, 10),
                -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    public void shouldMakeIntegerRangeWithSkips() {
        assertEqual(RangeIterables.ofRangeWithSkip(0, 10, 2),
                0, 2, 4, 6, 8, 10);
    }

    public void shouldMakeIntegerRangeWithSkipsMissingFinal() {
        assertEqual(RangeIterables.ofRangeWithSkip(0, 10, 3),
                0, 3, 6, 9);
    }

    public void shouldMakePowersOfTwo() {
        assertEqual(RangeIterables.powersOf(2, 1000L),
                1L, 2L, 4L, 8L, 16L, 32L, 64L, 128L, 256L, 512L);
    }

    private static <T extends Comparable<T>> void assertEqual(RangeIterable<T> rangeIterable, T... expectedArray) {
        List<T> actual = Lists.newArrayList(rangeIterable);
        List<T> expected = Lists.newArrayList(expectedArray);
        assertEquals(expected, actual);
    }

    public void shouldStopRatherThanOverflow() {
        long thirdMax = Long.MAX_VALUE / 3;

        assertEqual(RangeIterables.multiplesOf(thirdMax),
                0L, thirdMax, 2 * thirdMax, 3 * thirdMax);
    }

    public void shouldFibonacci() {
        assertEqual(RangeIterables.fibonacci(100L),
                0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L, 55L, 89L);
    }

}
