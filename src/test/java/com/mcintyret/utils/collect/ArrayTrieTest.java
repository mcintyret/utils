package com.mcintyret.utils.collect;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mcintyret.utils.random.RandomUtils;
import com.mcintyret.utils.serialize.JavaSerializer;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Iterables.elementsEqual;
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

    public void shouldGetSubTrie() {
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

        subtrie.put("fooaaa", new Object());
        subtrie.put("foozzz", new Object());

        assertEquals(Lists.newArrayList("fooaaa", "foozzz"), Lists.newArrayList(subtrie.keySet()));
        assertEquals(Lists.newArrayList("foozzz", "fooaaa"), Lists.newArrayList(subtrie.descendingKeySet()));

    }

    public void shouldGetFirstKey() {
        Trie<Object> trie = Tries.newSingleCaseArrayTrie();

        trie.put("foo", foo);
        trie.put("foobar", foobar);
        trie.put("bar", bar);
        trie.put("baz", baz);

        assertEquals("bar", trie.firstKey());
    }

    public void shouldGetLastKey() {
        Trie<Object> trie = Tries.newSingleCaseArrayTrie();

        trie.put("foo", foo);
        trie.put("foobar", foobar);
        trie.put("bar", bar);
        trie.put("baz", baz);

        assertEquals("foobar", trie.lastKey());
    }

    public void shouldIterateInReverseLexicographicOrder() {
        Trie<String> trie = Tries.newSingleCaseArrayTrie();
        for (int i = 0; i < 5000; i++) {
            String str = RandomStringUtils.randomAlphabetic(RandomUtils.nextIntBetween(2, 7));
            trie.put(str, str);
        }
        List<String> trieList = Lists.newArrayList(trie.descendingKeySet());
        System.out.println(trieList);
        List<String> sortedCopy = Lists.newArrayList(trieList);
        Collections.sort(sortedCopy, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));

        assertEquals(sortedCopy, trieList);
        assertEquals(trieList.size(), trie.size());
    }

    public void shouldGetSubMap() {
        Trie<String> trie = trieOf("aa", "aab", "foo", "foobar", "ku", "za");

        Trie<String> submap = trie.subMap("aaa", true, "ku", false);

        System.out.println(submap);
        assertEquals(3, submap.size());
        assertTrue(elementsEqual(Lists.newArrayList("aab", "foo", "foobar"), submap.keySet()));

        System.out.println(submap.descendingMap());
        assertTrue(elementsEqual(Lists.newArrayList("foobar", "foo", "aab"), submap.descendingKeySet()));

        submap.clear();
        System.out.println(submap);
        assertTrue(submap.isEmpty());

        assertEquals(3, trie.size());
        assertTrue(trie.containsKey("aa"));
        assertFalse(trie.containsKey("aab"));
        assertFalse(trie.containsKey("foo"));
        assertFalse(trie.containsKey("foobar"));
        assertTrue(trie.containsKey("ku"));
        assertTrue(trie.containsKey("za"));
    }

    public void shouldNavigate() {
        Trie<String> trie = trieOf("aa", "aab", "foo", "foobar", "ku", "za");

        assertEquals("foobar", trie.higherKey("foo"));
        assertEquals("za", trie.lastKey());
        assertEquals("aab", trie.ceilingKey("aab"));
        assertEquals("aab", trie.ceilingKey("aaa"));
        assertEquals("aa", trie.floorKey("aaa"));
        assertNull(trie.lowerKey("aa"));
        assertNull(trie.higherKey("za"));
        assertEquals("foo", trie.floorKey("foo"));
        assertEquals("foo", trie.lowerKey("foobar"));
        assertEquals("aa", trie.firstKey());
    }

    public void shouldSerializeAndDeserialize() {
        Trie<String> trie = trieOf("aa", "aab", "foo", "foobar", "ku", "za");

        JavaSerializer.getInstance().serialize(trie, "testFile");

        Trie<String> newTrie = JavaSerializer.getInstance().deserialize("testFile", Trie.class);

        assertEquals(trie, newTrie);
    }

    private static Trie<String> addToTrie(Trie<String> trie, String... toAdd) {
        for (String string : toAdd) {
            trie.put(string, string);
        }
        return trie;
    }

    private static Trie<String> trieOf(String... toAdd) {
        return addToTrie(Tries.<String>newSingleCaseArrayTrie(), toAdd);
    }


}
