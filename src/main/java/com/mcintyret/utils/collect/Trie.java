package com.mcintyret.utils.collect;

import com.google.common.base.Function;

import java.lang.reflect.Array;
import java.util.*;

import static com.google.common.base.Functions.toStringFunction;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Iterators.singletonIterator;
import static com.mcintyret.utils.Utils.valueIterator;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 */
public class Trie<V> extends AbstractMap<String, V> {

    private final TrieNode root;
    private final Function<V, String> keyFromValueFunction;

    private int size;

    public Trie() {
        this((Function<V, String>) toStringFunction());
    }

    public Trie(Function<V, String> keyFromValueFunction) {
        this.keyFromValueFunction = keyFromValueFunction;
        this.root = new TrieNode('\0');
    }

    public Trie(Map<String, ? extends V> map) {
        this();
        putAll(map);
    }

    public Trie(Collection<? extends V> c) {
        this();
        values().addAll(c);
    }

    public Trie(Map<String, ? extends V> map, Function<V, String> keyFromValueFunction) {
        this(keyFromValueFunction);
        putAll(map);
    }

    public Trie(Collection<? extends V> c, Function<V, String> keyFromValueFunction) {
        this(keyFromValueFunction);
        values().addAll(c);
    }

    public void add(V val) {
        checkNotNull(val);
        put(keyFromValueFunction.apply(val), val);
    }

    @Override
    public V put(String key, V val) {
        checkNotNull(key);
        checkNotNull(val);
        key = key.toUpperCase();
        TrieNode node = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (Character.isAlphabetic(c)) {
                TrieNode next = node.getChild(c);
                if (i == key.length() - 1) {
                    if (next == null) {
                        next = new TrieNode(c, val);
                    } else {
                        return next.setValue(val);
                    }
                } else if (next == null) {
                    next = new TrieNode(c);
                }
                node.setChild(c, next);
                node = next;
            }
        }
        size++;
        return null;
    }

    @Override
    public V get(Object o) {
        return get((String) o);
    }

    private V get(String key) {
        Iterator<V> iterator = getAll(key).iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    private TrieNode getNode(String key) {
        checkNotNull(key);
        if (key.isEmpty()) {
            return root;
        }
        key = key.toUpperCase();
        TrieNode node = root;

        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (Character.isAlphabetic(c)) {
                node = node.children[c - 'A'];
                if (node == null) {
                    return null;
                }
            }
        }
        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(Object key) {
        TrieNode node = getNode((String) key);
        return node != null && node.getKey() != null;
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            Arrays.fill(root.children, null);
            root.setValue(null);
            size = 0;
        }
    }

    @Override
    public V remove(Object key) {
        checkNotNull(key);
        return remove(((String) key).toUpperCase());
    }

    private V remove(String key) {
        V removed = doRemove(key);
        if (removed != null) {
            size--;
        }
        return removed;
    }

    private V doRemove(String key) {
        String substring = key.substring(0, key.length() - 1);
        TrieNode parent = getNode(substring);
        if (parent != null) {
            char lastChar = key.charAt(key.length() - 1);
            TrieNode existing = parent.getChild(lastChar);
            parent.setChild(lastChar, null);
            if (parent != root && parent.getValue() == null && parent.isEmpty()) {
                remove(substring);
            }
            return existing.getValue();
        }
        return null;
    }

    private Set<Entry<String, V>> entrySet;

    @Override
    public Set<Entry<String, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<Entry<String, V>>() {

                @Override
                public Iterator<Entry<String, V>> iterator() {
                    return new TrieIterator(root);
                }

                @Override
                public int size() {
                    return size;
                }
            };
        }
        return entrySet;
    }

    private Collection values;

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new AbstractCollection<V>() {

                @Override
                public boolean contains(Object o) {
                    return Trie.this.containsValue(o);
                }

                @Override
                public boolean add(V value) {
                    Trie.this.add(value);
                    return true;
                }

                @Override
                public boolean remove(Object o) {
                    if (o != null) {
                        String key = toKey(o);
                        TrieNode node = getNode(key);
                        if (node != null && o.equals(node.getValue())) {
                            // we do need to remove
                            doRemove(key);
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public Iterator<V> iterator() {
                    return valueIterator(new TrieIterator(root));
                }

                @Override
                public int size() {
                    return Trie.this.size();
                }
            };
        }
        return values;
    }

    @Override
    public boolean containsValue(Object val) {
        if (val != null) {
            TrieNode node = getNode(toKey(val));
            return node != null && val.equals(node.getValue());
        }
        return false;
    }

    public Iterable<V> getAll(String key) {
        final TrieNode node = getNode(key);
        return node == null ? Collections.<V>emptyList() : new Iterable<V>() {
            @Override
            public Iterator<V> iterator() {
                return valueIterator(new TrieIterator(node));
            }
        };
    }


    private class TrieIterator implements Iterator<Entry<String, V>> {

        TrieIterator(TrieNode node) {
            iterator = singletonIterator(node);
        }

        private Map.Entry<String, V> next = null;
        private Entry<String, V> lastReturned = null;
        private final Stack<Iterator<TrieNode>> iteratorStack = new Stack<>();
        private Iterator<TrieNode> iterator;

        @Override
        public boolean hasNext() {
            if (next == null) {
                while (iterator.hasNext()) {
                    TrieNode node = iterator.next();
                    iteratorStack.push(node.iterator());
                    if (node.getValue() != null) {
                        // This is an actual entry of interest
                        next = node;
                        return true;
                    }
                }
                if (!iteratorStack.isEmpty()) {
                    iterator = iteratorStack.pop();
                    return hasNext();
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Entry<String, V> next() {
            if (hasNext()) {
                lastReturned = next;
                next = null;
                return lastReturned;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            checkState(lastReturned != null);
            Trie.this.remove(lastReturned.getKey());
            lastReturned = null;
        }
    }

    private class TrieNode implements Entry<String, V>, Iterable<TrieNode> {
        private final char c;

        private final TrieNode[] children;

        private V val;

        private TrieNode(char c) {
            this(c, null);
        }

        private TrieNode(char c, V val) {
            this.c = c;
            this.val = val;
            this.children = (TrieNode[]) Array.newInstance(TrieNode.class, 26);
        }

        @Override
        public String getKey() {
            return keyFromValueFunction.apply(val);
        }

        @Override
        public V getValue() {
            return val;
        }

        @Override
        public V setValue(V value) {
            V oldVal = val;
            this.val = value;
            return oldVal;
        }

        public TrieNode getChild(char c) {
            return children[convert(c)];
        }

        public void setChild(char c, TrieNode child) {
            children[convert(c)] = child;
        }

        public boolean isEmpty() {
            for (TrieNode child : children) {
                if (child != null) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Iterator<TrieNode> iterator() {
            return filter(MoreIterators.forArray(children), notNull());
        }
    }

    private static char convert(char c) {
        return (char) (c - 'A');
    }

    private String toKey(Object o) {
        return keyFromValueFunction.apply((V) o);
    }
}
