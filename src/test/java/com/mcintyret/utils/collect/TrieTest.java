package com.mcintyret.utils.collect;

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertEquals;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 */

@Test
public class TrieTest {

    private final ValueClass foo = new ValueClass("foo");
    private final ValueClass foobar = new ValueClass("foobar");
    private final ValueClass bar = new ValueClass("bar");
    private final ValueClass baz = new ValueClass("baz");

    public void shouldAdd() {
        Trie<ValueClass> trie = new Trie<>(KEY_FROM_VALUE_FUCNTION);

        trie.add(foo);
        trie.add(foobar);
        trie.add(bar);
        trie.add(baz);

        assertEquals(4, trie.size());

        assertTrue(trie.containsKey("foo"));
        assertTrue(trie.containsKey("bar"));
        assertTrue(trie.containsKey("foobar"));
        assertTrue(trie.containsKey("baz"));

        assertFalse(trie.containsKey("quux"));
        assertFalse(trie.containsKey("barbaz"));
    }

    public void shouldOverwrite() {
        Trie<ValueClass> trie = new Trie<>(KEY_FROM_VALUE_FUCNTION);

        trie.add(foo);

        ValueClass newFoo = new ValueClass("foo");
        trie.add(newFoo);

        assertEquals(1, trie.size());

        assertSame(newFoo, trie.get("foo"));
        assertNotSame(foo, trie.get("foo"));
    }

    public void shouldGet() {
        Trie<ValueClass> trie = new Trie<>(KEY_FROM_VALUE_FUCNTION);

        trie.add(foo);
        trie.add(foobar);
        trie.add(bar);
        trie.add(baz);

        assertSame(foo, trie.get("foo"));
        assertSame(bar, trie.get("bar"));
        assertSame(foobar, trie.get("foobar"));
        assertSame(baz, trie.get("baz"));

        assertNull(trie.get("quux"));
        assertNull(trie.get("barbaz"));
    }

    public void shouldIterateValues() {
       Trie<ValueClass> trie = new Trie<>(KEY_FROM_VALUE_FUCNTION);

        trie.add(foo);
        trie.add(foobar);
        trie.add(bar);
        trie.add(baz);

        Set<ValueClass> set = Sets.newHashSet(trie.values()); // uses iterator

        assertEquals(4, set.size());
        assertTrue(set.contains(foo));
        assertTrue(set.contains(foobar));
        assertTrue(set.contains(bar));
        assertTrue(set.contains(baz));
    }

    public void shouldGetAll() {
        Trie<ValueClass> trie = new Trie<>(KEY_FROM_VALUE_FUCNTION);

        trie.add(foo);
        trie.add(foobar);
        trie.add(bar);
        trie.add(baz);

        Set<ValueClass> foos = Sets.newHashSet(trie.getAll("foo"));
        assertEquals(2, foos.size());
        assertTrue(foos.contains(foo));
        assertTrue(foos.contains(foobar));
    }

    public void shouldRemove() {

        Trie<ValueClass> trie = new Trie<>(KEY_FROM_VALUE_FUCNTION);

        trie.add(foo);
        trie.add(bar);

        assertEquals(2, trie.size());
        assertTrue(trie.containsKey("foo"));
        assertTrue(trie.containsKey("bar"));

        assertSame(foo, trie.remove("foo"));
        assertEquals(1, trie.size());
        assertFalse(trie.containsKey("foo"));
        assertTrue(trie.containsKey("bar"));

        assertNull(trie.remove("foobar"));
        assertEquals(1, trie.size());
        assertFalse(trie.containsKey("foo"));
        assertTrue(trie.containsKey("bar"));

        assertSame(bar, trie.remove("bar"));
        assertTrue(trie.isEmpty());

    }

    private static final Function<ValueClass, String> KEY_FROM_VALUE_FUCNTION = new Function<ValueClass, String>() {
        @Override
        public String apply(ValueClass input) {
            return input.key;
        }
    };

    private static class ValueClass {
        final String key;

        public ValueClass(String key) {
            this.key = key;
        }
    }
}
