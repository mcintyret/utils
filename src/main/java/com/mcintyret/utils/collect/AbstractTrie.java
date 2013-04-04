package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterators.singletonIterator;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 *
 * // TODO: make null-safe?
 */
public abstract class AbstractTrie<V> extends AbstractMap<String, V> implements Trie<V>, Serializable {

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

    private V remove(String key) {
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

    private Set<Entry<String, V>> entrySet;

    @Override
    public Set<Entry<String, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<Entry<String, V>>() {

                @Override
                public Iterator<Entry<String, V>> iterator() {
                    return new LexicographicTrieIterator(getRoot());
                }

                @Override
                public int size() {
                    return AbstractTrie.this.size();
                }
            };
        }
        return entrySet;
    }

    @Override
    public Trie<V> getSubTrie(String key) {
        TrieNode<V> node = getNode(key);
        return node == null ? Tries.<V>emptyTrie() : new Subtrie(key, node);
    }

    @Override
    public Trie<V> removeSubTrie(String key) {
        TrieNode<V> node = removeTrieNode(key);
        return node == null ? Tries.<V>emptyTrie() : new Subtrie(key, node);
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
            modifySize(-Iterators.size(new LexicographicTrieIterator(node, key)));
            return node;
        } else {
            return null;
        }
    }

    private class LexicographicTrieIterator extends AbstractHasNextFetchingIterator<Entry<String, V>> {

        private final Stack<Iterator<CharacterAndNode<V>>> iteratorStack = new Stack<>();
        private Iterator<CharacterAndNode<V>> iterator;

        LexicographicTrieIterator(TrieNode<V> root) {
            this(root, "");
        }

        LexicographicTrieIterator(TrieNode<V> node, String prefix) {
            key = Arrays.copyOf(prefix.toCharArray(), 10);
            index = prefix.length();
            iterator = node == null ? Iterators.<CharacterAndNode<V>>emptyIterator() :
                    singletonIterator(new CharacterAndNode<>('\0', node));
        }

        private char[] key;
        private int index;

        @Override
        public boolean doHasNext() {
            while (iterator.hasNext()) {
                final CharacterAndNode<V> charAndNode = iterator.next();
                updateKey(charAndNode.getChar());
                iteratorStack.push(iterator);
                iterator = charAndNode.getNode().iterator();
                if (charAndNode.getNode().getValue() != null) {
                    // This is an actual entry of interest
                    setNext(new Entry<String, V>() {

                        final String thisKey = new String(key, 0, index);

                        @Override
                        public String getKey() {
                            return thisKey;
                        }

                        @Override
                        public V getValue() {
                            return charAndNode.getNode().getValue();
                        }

                        @Override
                        public V setValue(V value) {
                            return charAndNode.getNode().setValue(value);
                        }
                    });
                    return true;
                }
            }
            if (!iteratorStack.isEmpty()) {
                iterator = iteratorStack.pop();
                index--;
                return doHasNext();
            } else {
                return false;
            }
        }

        private void updateKey(char c) {
            if (c != '\0') {
                if (index == key.length) {
                    key = Arrays.copyOf(key, key.length * 2);
                }
                key[index++] = c;
            }
        }


        @Override
        protected void doRemove(Entry<String, V> removed) {
            AbstractTrie.this.remove(removed.getKey());
        }
    }

    private class Subtrie extends AbstractMap<String, V> implements Trie<V> {

        private final String prefix;

        private TrieNode<V> root;

        public Subtrie(String prefix, TrieNode<V> root) {
            this.prefix = prefix;
            this.root = root;
        }

        @Override
        public boolean containsKey(Object key) {
            return get(key) != null;
        }

        @Override
        public V put(String key, V val) {
            checkNotNull(key);
            checkNotNull(val);
            checkArgument(checkKey(key));
            TrieNode<V> node = getSubtrieNode(key, true);
            V prev = node.setValue(val);
            if (prev == null) {
                AbstractTrie.this.modifySize(+1);
            }
            return prev;
        }

        @Override
        public V get(Object key) {
            TrieNode<V> node = getSubtrieNode((String) key, false);
            return node == null ? null : node.getValue();
        }

        @Override
        public V remove(Object key) {
            return remove((String) key);
        }

        private V remove(String key) {
            checkArgument(checkKey(key));
            return AbstractTrie.this.remove(key);
        }

        private TrieNode<V> getSubtrieNode(String key, boolean create) {
            if (!checkKey(key)) {
                return null;
            }
            if (root == null && create) {
                root = newTrieNode(null);
            }
            return AbstractTrie.this.getNode(root, key, prefix.length(), key.length() - 1, create);
        }

        @Override
        public int size() {
            return entrySet().size();
        }

        @Override
        public Set<Entry<String, V>> entrySet() {
            return new AbstractSet<Entry<String, V>>() {
                @Override
                public Iterator<Entry<String, V>> iterator() {
                    return new LexicographicTrieIterator(root, prefix);
                }

                @Override
                public int size() {
                    return Iterators.size(iterator());
                }
            };
        }

        @Override
        public Trie<V> getSubTrie(String key) {
            return checkKey(key) ? AbstractTrie.this.getSubTrie(key) : Tries.<V>emptyTrie();
        }

        @Override
        public Trie<V> removeSubTrie(String key) {
            return checkKey(key) ? AbstractTrie.this.removeSubTrie(key) : Tries.<V>emptyTrie();
        }

        @Override
        public void clear() {
            AbstractTrie.this.removeSubTrie(prefix);
            root = null;
        }

        private boolean checkKey(String key) {
            return key.startsWith(prefix);
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
}
