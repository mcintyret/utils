package com.mcintyret.utils.collect;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mcintyret.utils.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.testng.AssertJUnit.*;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 */

@Test
public class ArrayTrieTest {

    private final Object foo = new Object();
    private final Object foobar = new Object();
    private final Object bar = new Object();
    private final Object baz = new Object();

    public void shouldAdd() {
        Trie<Object> trie = Tries.newSingleCaseArrayTrie();

        trie.put("foo", foo);
        trie.put("foobar", foobar);
        trie.put("bar", bar);
        trie.put("baz", baz);

        assertEquals(4, trie.size());

        assertTrue(trie.containsKey("foo"));
        assertTrue(trie.containsKey("bar"));
        assertTrue(trie.containsKey("foobar"));
        assertTrue(trie.containsKey("baz"));

        assertFalse(trie.containsKey("quux"));
        assertFalse(trie.containsKey("barbaz"));
    }

    public void shouldOverwrite() {
        Trie<Object> trie = Tries.newSingleCaseArrayTrie();

        trie.put("foo", foo);

        Object newFoo = new Object();
        trie.put("foo", newFoo);

        assertEquals(1, trie.size());

        assertSame(newFoo, trie.get("foo"));
        assertNotSame(foo, trie.get("foo"));
    }

    public void shouldGet() {
        Trie<Object> trie = Tries.newSingleCaseArrayTrie();

        trie.put("foo", foo);
        trie.put("foobar", foobar);
        trie.put("bar", bar);
        trie.put("baz", baz);

        assertSame(foo, trie.get("foo"));
        assertSame(bar, trie.get("bar"));
        assertSame(foobar, trie.get("foobar"));
        assertSame(baz, trie.get("baz"));

        assertNull(trie.get("quux"));
        assertNull(trie.get("barbaz"));
    }

    public void shouldIterateValues() {
        Trie<Object> trie = Tries.newSingleCaseArrayTrie();

        trie.put("foo", foo);
        trie.put("foobar", foobar);
        trie.put("bar", bar);
        trie.put("baz", baz);

        Set<Object> set = Sets.newHashSet(trie.values()); // uses iterator

        assertEquals(4, set.size());
        assertTrue(set.contains(foo));
        assertTrue(set.contains(foobar));
        assertTrue(set.contains(bar));
        assertTrue(set.contains(baz));
    }

    public void shouldGetAll() {
        Trie<Object> trie = Tries.newSingleCaseArrayTrie();

        trie.put("foo", foo);
        trie.put("foobar", foobar);
        trie.put("bar", bar);
        trie.put("baz", baz);

        Set<Object> foos = Sets.newHashSet(trie.getSubTrie("foo").values());
        assertEquals(2, foos.size());
        assertTrue(foos.contains(foo));
        assertTrue(foos.contains(foobar));
    }

    public void shouldRemove() {

        Trie<Object> trie = Tries.newSingleCaseArrayTrie();

        trie.put("foo", foo);
        trie.put("foobar", foobar);

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
            String str = RandomStringUtils.randomAlphabetic(RandomUtils.nextIntBetween(2, 7));
            trie.put(str, str);
        }
        List<String> trieList = Lists.newArrayList(trie.values());
        System.out.println(trieList);
        List<String> sortedCopy = Lists.newArrayList(trieList);
        Collections.sort(sortedCopy, String.CASE_INSENSITIVE_ORDER);

        assertEquals(sortedCopy, trieList);
        assertEquals(trieList.size(), trie.size());
    }

    public void shouldMakeSubtrie() {
        Trie<Object> trie = Tries.newSingleCaseArrayTrie();

        trie.put("foo", foo);
        trie.put("foobar", foobar);
        trie.put("bar", bar);
        trie.put("baz", baz);

        Trie<Object> subtrie = trie.getSubTrie("foo");
        assertEquals(2, subtrie.size());
        assertTrue(subtrie.containsKey("foo"));
        assertTrue(subtrie.containsKey("foobar"));
        assertFalse(subtrie.containsKey("bar"));

        subtrie.put("fooble", new Object());
        assertEquals(3, subtrie.size());
        assertEquals(5, trie.size());

        assertTrue(trie.containsKey("fooble"));

        subtrie.clear();
        assertTrue(subtrie.isEmpty());
        assertEquals(2, trie.size());
        assertTrue(trie.containsKey("bar"));
        assertTrue(trie.containsKey("baz"));
        assertFalse(trie.containsKey("foo"));
        assertFalse(trie.containsKey("foobar"));
        assertFalse(trie.containsKey("fooble"));

    }

}
