package com.mcintyret.utils.collect;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterators.singletonIterator;
import static com.mcintyret.utils.Utils.valueIterator;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 */
public abstract class AbstractTrie<V> extends AbstractMap<String, V> implements Trie<V> {

    protected final TrieNode root;
    private final Function<V, String> keyFromValueFunction;

    private final Sizer sizer;

    protected void modifySize(int delta) {
        sizer.modifySize(delta);
    }

    protected void resetSize() {
        sizer.resetSize();
    }

    @Override
    public int size() {
        return sizer.getSize();
    }

    // For creating subtries
    private AbstractTrie(TrieNode root, Function<V, String> keyFromValueFunction) {
        this.root = root;
        this.keyFromValueFunction = keyFromValueFunction;
        this.sizer = null;
    }

    protected AbstractTrie(Function<V, String> keyFromValueFunction, Sizer sizer) {
        this.keyFromValueFunction = keyFromValueFunction;
        this.sizer = sizer;
        this.root = newTrieNode();
    }

    protected AbstractTrie(Function<V, String> keyFromValueFunction) {
        this(keyFromValueFunction, new SimpleSizer());
    }

    protected abstract TrieNode newTrieNode(V value);

    private TrieNode newTrieNode() {
        return newTrieNode(null);
    }

    protected boolean isCharacterInteresting(char c) {
        // simple default returning true for all characters.
        return true;
    }

    @Override
    public void add(V val) {
        checkNotNull(val);
        put(keyFromValueFunction.apply(val), val);
    }

    @Override
    public V put(String key, V val) {
        checkNotNull(key);
        checkNotNull(val);
        TrieNode node = getNode(key, true);
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
        TrieNode node = getNode(key);
        return node == null ? null : node.getValue();
    }


    private final TrieNode getNode(TrieNode node, String key, int from, int to, boolean create) {
        checkNotNull(key);

        for (int i = from; i < to; i++) {
            char c = key.charAt(i);
            if (isCharacterInteresting(c)) {
                TrieNode next = node.getChild(c);
                if (next == null) {
                    if (create) {
                        next = newTrieNode();
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

    protected TrieNode getNode(String key, int to, boolean create) {
        return getNode(root, key, 0, to, create);
    }

    protected TrieNode getNode(String key, boolean create) {
        return getNode(key, key.length(), create);
    }

    private final TrieNode getNode(String key) {
        return getNode(key, false);
    }


    @Override
    public boolean containsKey(Object key) {
        TrieNode node = getNode((String) key);
        return node != null && node.getKey() != null;
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            root.clear();
            root.setValue(null);
            resetSize();
        }
    }

    @Override
    public V remove(Object key) {
        checkNotNull(key);
        return remove((String) key);
    }

    private V remove(String key) {
        V removed = doRemove(key);
        if (removed != null) {
            modifySize(-1);
        }
        return removed;
    }

    private V doRemove(String key) {
        return doRemove(key, key.length() - 1);
    }

    private V doRemove(String key, int end) {
        while(!isCharacterInteresting(key.charAt(end))) {
            end--;
        }
        TrieNode parent = getNode(key, end, false);
        if (parent != null) {
            char lastChar = key.charAt(end);
            TrieNode child = parent.getChild(lastChar);
            if (child.isEmpty()) {
                parent.setChild(lastChar, null);
                if (parent != root && parent.getValue() == null) {
                    doRemove(key, end - 1);
                }
                return child.getValue();
            } else {
                return child.setValue(null);
            }
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
                    return AbstractTrie.this.size();
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
                    return AbstractTrie.this.containsValue(o);
                }

                @Override
                public boolean add(V value) {
                    AbstractTrie.this.add(value);
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
                    return valueIterator(new LexicographicTrieIterator(root));
                }

                @Override
                public int size() {
                    return AbstractTrie.this.size();
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


    // TODO: return empty Trie rather than null
    @Override
    public Trie<V> subTrie(String key) {
        TrieNode node = getNode(key);
        return node == null ? Tries.<V>emptyTrie() : new Subtrie<>(key, node, this, keyFromValueFunction);
    }


    private class TrieIterator extends AbstractHasNextFetchingIterator<Entry<String, V>> {

        TrieIterator(TrieNode node) {
            iterator = singletonIterator(node);
        }

        private final Queue<Iterator<TrieNode>> iteratorQueue = new ArrayDeque<>();
        private Iterator<TrieNode> iterator;

        @Override
        public boolean doHasNext() {
            while (iterator.hasNext()) {
                TrieNode node = iterator.next();
                iteratorQueue.offer(node.iterator());
                if (node.getValue() != null) {
                    // This is an actual entry of interest
                    setNext(node);
                    return true;
                }
            }
            if (!iteratorQueue.isEmpty()) {
                iterator = iteratorQueue.poll();
                return doHasNext();
            } else {
                return false;
            }
        }

        @Override
        protected void doRemove(Entry<String, V> removed) {
            AbstractTrie.this.remove(removed.getKey());
        }
    }

    private class LexicographicTrieIterator extends AbstractHasNextFetchingIterator<Entry<String, V>> {

        LexicographicTrieIterator(TrieNode node) {
            iterator = singletonIterator(node);
        }

        private final Stack<Iterator<TrieNode>> iteratorStack = new Stack<>();
        private Iterator<TrieNode> iterator;

        @Override
        public boolean doHasNext() {
            while (iterator.hasNext()) {
                TrieNode node = iterator.next();
                iteratorStack.push(iterator);
                iterator = node.iterator();
                if (node.getValue() != null) {
                    // This is an actual entry of interest
                    setNext(node);
                    return true;
                }
            }
            if (!iteratorStack.isEmpty()) {
                iterator = iteratorStack.pop();
                return doHasNext();
            } else {
                return false;
            }
        }

        @Override
        protected void doRemove(Entry<String, V> removed) {
            AbstractTrie.this.remove(removed.getKey());
        }
    }

    protected abstract class TrieNode implements Entry<String, V>, Iterable<TrieNode> {

        private V val;

        protected TrieNode(V val) {
            this.val = val;
        }

        @Override
        public String getKey() {
            return val == null ? null : keyFromValueFunction.apply(val);
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

        protected abstract TrieNode getChild(char c);

        protected abstract void setChild(char c, TrieNode child);

        protected abstract boolean isEmpty();

        protected abstract void clear();

    }

    private String toKey(Object o) {
        return keyFromValueFunction.apply((V) o);
    }

    private static class Subtrie<V> extends AbstractTrie<V> {

        private final String prefix;

        private final AbstractTrie<V> backingTrie;

        public Subtrie(String prefix, TrieNode root, AbstractTrie<V> trie, Function<V, String> keyFromValueFunction) {
            super(root, keyFromValueFunction);
            this.prefix = prefix;
            this.backingTrie = trie;
        }

        @Override
        protected void modifySize(int delta) {
            backingTrie.modifySize(delta);
        }

        @Override
        protected void resetSize() {
            backingTrie.resetSize();
        }

        @Override
        protected TrieNode newTrieNode(V value) {
            return backingTrie.newTrieNode(value);
        }

        @Override
        protected boolean isCharacterInteresting(char c) {
            return backingTrie.isCharacterInteresting(c);
        }

        @Override
        protected TrieNode getNode(String key, int to, boolean create) {
            if (!key.startsWith(prefix)) {
                if (create) {
                    throw new IllegalArgumentException();
                } else {
                    return null;
                }
            }
            return backingTrie.getNode(this.root, key, prefix.length(), to, create);
        }

        @Override
        public int size() {
            return Iterators.size(entrySet().iterator());
        }

    }

}
