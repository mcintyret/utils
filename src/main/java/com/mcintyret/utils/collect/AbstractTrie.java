package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.mcintyret.utils.Comparables;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 * <p/>
 * // TODO: make null-safe?
 */
public abstract class AbstractTrie<V> extends AbstractNavigableMap<String, V> implements Trie<V>, Serializable {

    protected abstract TrieNode<V> newTrieNode(V value);

    protected boolean isCharacterInteresting(char c) {
        // simple default returning true for all characters.
        return true;
    }

    private int size = 0;

    private TrieNode<V> root;

    protected void modifySize(int delta) {
        size += delta;
    }

    protected void setRootIfNull() {
        if (root == null) {
            root = newTrieNode(null);
        }
    }

    protected TrieNode<V> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public V put(String key, V val) {
        checkNotNull(key);
        checkNotNull(val);
        TrieNode<V> node = getNode(stripUninterestingChars(key), true);
        V prev = node.setValue(val);
        if (prev == null) {
            modifySize(+1);
        }
        return prev;
    }

    @Override
    public V get(Object o) {
        return get((String) o);
    }

    private V get(String key) {
        TrieNode<V> node = getNode(stripUninterestingChars(key));
        return node == null ? null : node.getValue();
    }


    private TrieNode<V> getNode(TrieNode<V> node, String key, int from, int to, boolean create) {
        if (node == null) {
            return null;
        }

        for (int i = from; i <= to; i++) {
            char c = key.charAt(i);
            if (isCharacterInteresting(c)) {
                TrieNode<V> next = node.getChild(c);
                if (next == null) {
                    if (create) {
                        next = newTrieNode(null);
                        node.setChild(c, next);
                    } else {
                        return null;
                    }
                }
                node = next;
            }
        }
        return node;
    }

    protected TrieNode<V> getNode(String key, int to, boolean create) {
        if (create) {
            setRootIfNull();
        }
        return getNode(getRoot(), key, 0, to, create);
    }

    protected TrieNode<V> getNode(String key, boolean create) {
        return getNode(key, key.length() - 1, create);
    }

    private TrieNode<V> getNode(String key) {
        return getNode(key, false);
    }


    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public V remove(Object key) {
        checkNotNull(key);
        return remove((String) key);
    }

    V remove(String key) {
        V removed = doRemove(stripUninterestingChars(key));
        if (removed != null) {
            modifySize(-1);
        }
        return removed;
    }

    private V doRemove(String key) {
        return doRemove(key, key.length() - 1);
    }

    private V doRemove(String key, int end) {
        TrieNode<V> parent = getNode(key, end - 1, false);
        if (parent != null) {
            char lastChar = key.charAt(end);
            TrieNode<V> child = parent.getChild(lastChar);
            if (child != null) {
                if (child.isEmpty()) {
                    parent.setChild(lastChar, null);
                    if (parent != getRoot() && parent.getValue() == null) {
                        doRemove(key, end - 1);
                    }
                    return child.getValue();
                } else {
                    return child.setValue(null);
                }
            }
        }
        return null;
    }

    @Override
    public Trie<V> getSubTrie(String key) {
        TrieNode<V> node = getNode(key);
        return node == null ? Tries.<V>emptyTrie() : new SubTrie(node, key);
    }

    @Override
    public Trie<V> removeSubTrie(String key) {
        TrieNode<V> node = removeTrieNode(key);
        return node == null ? Tries.<V>emptyTrie() : new SubTrie(node, key);
    }

    private TrieNode<V> removeTrieNode(String key) {
        if (key.isEmpty()) {
            return getRoot();
        }
        TrieNode<V> parent = key.length() == 1 ? getRoot() : getNode(key, key.length() - 2, false);
        if (parent != null) {
            char c = key.charAt(key.length() - 1);
            TrieNode<V> node = parent.getChild(c);
            if (node != null) {
                parent.setChild(c, null);
            }
            modifySize(-Iterators.size(new LexicographicTrieIterator<>(this, node, key)));
            return node;
        } else {
            return null;
        }
    }

    private String stripUninterestingChars(String key) {
        char[] chars = new char[key.length()];
        int index = 0;
        for (char c : key.toCharArray()) {
            if (isCharacterInteresting(c)) {
                chars[index++] = c;
            }
        }
        return new String(chars, 0, index);
    }

    @Override
    public Comparator<String> comparator() {
        return THE_COMPARATOR;
    }

    @Override
    public Trie<V> descendingMap() {
        return new DescendingTrie();
    }

    @Override
    public Trie<V> subMap(String from, boolean fromInclusive, String to, boolean toInclusive) {
        return new NavigableSubMap(from, fromInclusive, to, toInclusive);
    }

    @Override
    public Trie<V> headMap(String to, boolean toInclusive) {
        return new NavigableSubMap(null, true, to, toInclusive);
    }

    @Override
    public Trie<V> tailMap(String from, boolean fromInclusive) {
        return new NavigableSubMap(from, fromInclusive, null, true);
    }

    private class DescendingTrie extends DescendingMap<String, V> implements Trie<V> {

        @Override
        protected Trie<V> forward() {
            return AbstractTrie.this;
        }

        @Override
        protected Iterator<Entry<String, V>> entryIterator() {
            return descendingEntryIterator();
        }

        @Override
        public Trie<V> getSubTrie(String key) {
            return AbstractTrie.this.getSubTrie(key);
        }

        @Override
        public Trie<V> removeSubTrie(String key) {
            return AbstractTrie.this.removeSubTrie(key);
        }

        @Override
        public Trie<V> descendingMap() {
            return forward();
        }

        @Override
        public Trie<V> subMap(String from, boolean fromInclusive, String to, boolean toInclusive) {
            return (Trie<V>) super.subMap(from, fromInclusive, to, toInclusive);
        }

        @Override
        public Trie<V> headMap(String to, boolean toInclusive) {
            return (Trie<V>) super.headMap(to, toInclusive);
        }

        @Override
        public Trie<V> tailMap(String from, boolean fromInclusive) {
            return (Trie<V>) super.tailMap(from, fromInclusive);
        }
    }

    @Override
    protected Iterator<Entry<String, V>> entryIterator() {
        return new LexicographicTrieIterator<>(this);
    }

    @Override
    protected Iterator<Entry<String, V>> descendingEntryIterator() {
        return new ReverseTrieIterator<>(this);
    }

    abstract class AbstractSubTrie extends AbstractTrie<V> {

        private boolean validKey(Object o) {
            return validKey((String) o);
        }

        abstract boolean validKey(String key);

        @Override
        protected TrieNode<V> newTrieNode(V value) {
            return AbstractTrie.this.newTrieNode(value);
        }

        @Override
        public int size() {
            return Iterators.size(entryIterator());
        }

        @Override
        protected void modifySize(int delta) {
            AbstractTrie.this.modifySize(delta);
        }

        @Override
        protected TrieNode<V> getRoot() {
            return AbstractTrie.this.getRoot();
        }

        @Override
        protected void setRootIfNull() {
            AbstractTrie.this.setRootIfNull();
        }

        @Override
        public V get(Object key) {
            return validKey(key) ? AbstractTrie.this.get(key) : null;
        }

        @Override
        public boolean containsKey(Object key) {
            return validKey(key) && AbstractTrie.this.containsKey(key);
        }

        @Override
        public Comparator<String> comparator() {
            return AbstractTrie.this.comparator();
        }

        @Override
        public SortedMap<String, V> subMap(String fromKey, String toKey) {
            if (!validKey(fromKey) || !validKey(toKey)) {
                throw new IllegalArgumentException("Invalid keys");
            } else {
                return AbstractTrie.this.subMap(fromKey, toKey);
            }
        }

        @Override
        public boolean isEmpty() {
            return !entrySet().iterator().hasNext();
        }

    }

    private class SubTrie extends AbstractSubTrie {

        private final String prefix;
        private TrieNode root;

        public SubTrie(TrieNode root, String prefix) {
            this.prefix = prefix;
            this.root = root;
        }

        @Override
        boolean validKey(String key) {
            return key.startsWith(prefix);
        }

        @Override
        protected TrieNode<V> getRoot() {
            return root;
        }

        @Override
        protected void setRootIfNull() {
            if (getRoot() == null) {
                root = newTrieNode(null);
            }
        }

        @Override
        protected TrieNode<V> getNode(String key, int to, boolean create) {
            if (create) {
                setRootIfNull();
            }
            return AbstractTrie.this.getNode(getRoot(), key, prefix.length(), to, create);
        }

        @Override
        public void clear() {
            AbstractTrie.this.removeSubTrie(prefix);
            root = null;
        }

        @Override
        protected Iterator<Entry<String, V>> entryIterator() {
            return new LexicographicTrieIterator<>(AbstractTrie.this, root, prefix);
        }

        @Override
        protected Iterator<Entry<String, V>> descendingEntryIterator() {
            return new ReverseTrieIterator<>(AbstractTrie.this, root, prefix);
        }

    }

    private class NavigableSubMap extends AbstractSubTrie {
        private final String from;
        private final boolean fromInclusive;
        private final String to;
        private final boolean toInclusive;
        private final boolean fromBottom;
        private final boolean toTop;


        // No fucking idea why this complains, but it compiles and runs OK...
        public NavigableSubMap(String from, boolean fromInclusive, String to, boolean toInclusive) {
            this.from = from;
            this.fromInclusive = fromInclusive;
            this.fromBottom = from == null;
            this.to = to;
            this.toInclusive = toInclusive;
            this.toTop = to == null;
        }

        @Override
        boolean validKey(String key) {
            return validBottom(key) &&
                    validTop(key);
        }

        private boolean validTop(String key) {
            return toTop || (toInclusive ? key.compareTo(to) <= 0 : key.compareTo(to) < 0);
        }

        private boolean validBottom(String key) {
            return fromBottom || (fromInclusive ? from.compareTo(key) <= 0 : from.compareTo(key) < 0);
        }

        @Override
        protected Iterator<Entry<String, V>> entryIterator() {
            return fromBottom ? new ForwardBoundIterator() : new ForwardBoundIterator(from);
        }

        private class ForwardBoundIterator extends AbstractIterator<Entry<String, V>> {

            private final PeekingIterator<Entry<String, V>> delegate;

            ForwardBoundIterator() {
                this("");
            }

            ForwardBoundIterator(String start) {
                delegate = new LexicographicTrieIterator<>(AbstractTrie.this, start);
                while (delegate.hasNext() && !validBottom(delegate.peek().getKey())) {
                    delegate.next();
                }
            }

            @Override
            protected Entry<String, V> computeNext() {
                if (delegate.hasNext() && validTop(delegate.peek().getKey())) {
                    return delegate.next();
                } else {
                    return endOfData();
                }
            }

            @Override
            protected void doRemove(Entry<String, V> removed) {
                delegate.remove();
            }
        }

        @Override
        protected Iterator<Entry<String, V>> descendingEntryIterator() {
            return toTop ? new ReverseBoundIterator() : new ReverseBoundIterator(to);
        }

        private class ReverseBoundIterator extends AbstractIterator<Entry<String, V>> {

            private final PeekingIterator<Entry<String, V>> delegate;

            ReverseBoundIterator() {
                this("");
            }

            ReverseBoundIterator(String start) {
                delegate = new ReverseTrieIterator<>(AbstractTrie.this, start);
                while (delegate.hasNext() && !validTop(delegate.peek().getKey())) {
                    delegate.next();
                }
            }

            @Override
            protected Entry<String, V> computeNext() {
                if (delegate.hasNext() && validBottom(delegate.peek().getKey())) {
                    return delegate.next();
                } else {
                    return endOfData();
                }
            }

            @Override
            protected void doRemove(Entry<String, V> removed) {
                delegate.remove();
            }
        }

        @Override
        public void clear() {
            MoreIterators.clear(entryIterator());
        }
    }

    private static final Comparator<String> THE_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    };

}
