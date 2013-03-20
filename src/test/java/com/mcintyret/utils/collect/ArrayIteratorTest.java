package com.mcintyret.utils.collect;

import org.testng.annotations.Test;

import java.util.ListIterator;

import static org.testng.AssertJUnit.*;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */

@Test
public class ArrayIteratorTest {

    public void shouldIterateNextAndPrevious() {
        ListIterator<String> it = new ArrayIterator<>("foo", "bar", "baz");

        assertFalse(it.hasPrevious());

        assertTrue(it.hasNext());
        assertEquals("foo", it.next());

        assertTrue(it.hasPrevious());
        assertEquals("foo", it.previous());

        assertEquals("foo", it.next());

        assertTrue(it.hasNext());
        assertEquals("bar", it.next());

        assertTrue(it.hasNext());
        assertEquals("baz", it.next());

        assertFalse(it.hasNext());
    }

     public void shouldSet() {
        String[] array = {"foo", "bar", "baz"};
        ListIterator<String> it = new ArrayIterator<>(array);

        assertEquals("foo", it.next());
        assertEquals("bar", it.next());

        it.set("quux");

        assertEquals("quux", array[1]);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldFailSettingNoNext() {
        ListIterator<String> it = new ArrayIterator<>("foo", "bar");

        it.set("quux");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void shouldFailRemove() {
        ListIterator<String> it = new ArrayIterator<>("foo", "bar");
        it.next();

        it.remove();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void shouldFailAdd() {
        ListIterator<String> it = new ArrayIterator<>("foo", "bar");
        it.next();

        it.add("quux");
    }


    public void shouldMakeEmptyIterator() {
        ListIterator<String> it = new ArrayIterator<>();

        assertFalse(it.hasNext());
        assertFalse(it.hasPrevious());
    }

}
