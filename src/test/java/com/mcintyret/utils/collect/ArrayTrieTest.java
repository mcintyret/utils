package com.mcintyret.utils.collect;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mcintyret.utils.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.testng.AssertJUnit.*;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 */

@Test
public class ArrayTrieTest {

    private final ValueClass foo = new ValueClass("foo");
    private final ValueClass foobar = new ValueClass("foobar");
    private final ValueClass bar = new ValueClass("bar");
    private final ValueClass baz = new ValueClass("baz");

    public void shouldAdd() {
        Trie<ValueClass> trie = Tries.newSingleCaseArrayTrie(KEY_FROM_VALUE_FUNCTION);

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
        Trie<ValueClass> trie = Tries.newSingleCaseArrayTrie(KEY_FROM_VALUE_FUNCTION);

        trie.add(foo);

        ValueClass newFoo = new ValueClass("foo");
        trie.add(newFoo);

        assertEquals(1, trie.size());

        assertSame(newFoo, trie.get("foo"));
        assertNotSame(foo, trie.get("foo"));
    }

    public void shouldGet() {
        Trie<ValueClass> trie = Tries.newSingleCaseArrayTrie(KEY_FROM_VALUE_FUNCTION);

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
        Trie<ValueClass> trie = Tries.newSingleCaseArrayTrie(KEY_FROM_VALUE_FUNCTION);

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
        Trie<ValueClass> trie = Tries.newSingleCaseArrayTrie(KEY_FROM_VALUE_FUNCTION);

        trie.add(foo);
        trie.add(foobar);
        trie.add(bar);
        trie.add(baz);

        Set<ValueClass> foos = Sets.newHashSet(trie.subTrie("foo").values());
        assertEquals(2, foos.size());
        assertTrue(foos.contains(foo));
        assertTrue(foos.contains(foobar));
    }

    public void shouldRemove() {

        Trie<ValueClass> trie = Tries.newSingleCaseArrayTrie(KEY_FROM_VALUE_FUNCTION);

        trie.add(foo);
        trie.add(foobar);

        assertEquals(2, trie.size());
        assertTrue(trie.containsKey("foo"));
        assertTrue(trie.containsKey("foobar"));

        assertSame(foo, trie.remove("foo"));
        assertEquals(1, trie.size());
        assertFalse(trie.containsKey("foo"));
        assertTrue(trie.containsKey("foobar"));

        assertNull(trie.remove("bar"));
        assertEquals(1, trie.size());
        assertFalse(trie.containsKey("foo"));
        assertTrue(trie.containsKey("foobar"));

        assertSame(foobar, trie.remove("foobar"));
        assertTrue(trie.isEmpty());

    }

    public void shouldIterateInLexicographicOrder() {
        Trie<String> trie = Tries.newSingleCaseArrayTrie();
        for (int i = 0; i < 5000; i++) {
            trie.add(RandomStringUtils.randomAlphabetic(RandomUtils.nextIntBetween(2, 7)).toUpperCase());
        }
        List<String> trieList = Lists.newArrayList(trie.values());
        List<String> sortedCopy = Lists.newArrayList(trieList);
//        Collections.sort(sortedCopy, TRIE_ORDER_COMPARATOR);
        Collections.sort(sortedCopy);

        assertEquals(sortedCopy, trieList);
        assertEquals(trieList.size(), trie.size());
    }

    public void shouldMakeSubtrie() {
        Trie<ValueClass> trie = Tries.newSingleCaseArrayTrie(KEY_FROM_VALUE_FUNCTION);

        trie.add(foo);
        trie.add(foobar);
        trie.add(bar);
        trie.add(baz);

        Trie<ValueClass> subtrie = trie.subTrie("foo");
        assertEquals(2, subtrie.size());
        assertTrue(subtrie.containsKey("foo"));
        assertTrue(subtrie.containsKey("foobar"));
        assertFalse(subtrie.containsKey("bar"));

        subtrie.add(new ValueClass("fooble"));
        assertEquals(3, subtrie.size());
        assertEquals(5, trie.size());

        assertTrue(trie.containsKey("fooble"));

    }

    private static final Function<ValueClass, String> KEY_FROM_VALUE_FUNCTION = new Function<ValueClass, String>() {
        @Override
        public String apply(ValueClass input) {
            return input.key;
        }
    };

    private static final Comparator<String> TRIE_ORDER_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            int len = o1.length() - o2.length();
            return len == 0 ? o1.compareTo(o2) : len;
        }
    };

    private static class ValueClass {
        final String key;

        public ValueClass(String key) {
            this.key = key;
        }
    }
}
