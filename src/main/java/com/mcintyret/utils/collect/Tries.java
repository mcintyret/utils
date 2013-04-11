package com.mcintyret.utils.collect;

import com.google.common.collect.PeekingIterator;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public final class Tries {

    private Tries() {

    }

    public static <T> Trie<T> emptyTrie() {
        return (Trie<T>) EmptyTrie.INSTANCE;
    }

    public static <T> Trie<T> newAsciiArrayTrie() {
        return new StrategyFollowingArrayTrie<>(ArrayTrieStrategy.ASCII);
    }

    public static <T> Trie<T> newSingleCaseArrayTrie() {
        return new StrategyFollowingArrayTrie<>(ArrayTrieStrategy.SINGLE_CASE);
    }

    public static <T> Trie<T> newSingleCaseNumericArrayTrie() {
        return new StrategyFollowingArrayTrie<>(ArrayTrieStrategy.SINGLE_CASE_NUMERIC);
    }

    public static <T> Trie<T> newMixedCaseArrayTrie() {
        return new StrategyFollowingArrayTrie<>(ArrayTrieStrategy.MIXED_CASE);
    }

    public static <T> Trie<T> newMixedCaseNumericArrayTrie() {
        return new StrategyFollowingArrayTrie<>(ArrayTrieStrategy.MIXED_CASE_NUMERIC);
    }

    public static <T> Trie<T> newConcurrentAsciiArrayTrie() {
        return new VeryConcurrentTrie<>(new StrategyFollowingArrayTrie<T>(ArrayTrieStrategy.ASCII));
    }

    public static <T> Trie<T> newConcurrentSingleCaseArrayTrie() {
        return new VeryConcurrentTrie<>(new StrategyFollowingArrayTrie<T>(ArrayTrieStrategy.SINGLE_CASE));
    }

    public static <T> Trie<T> newConcurrentSingleCaseNumericArrayTrie() {
        return new VeryConcurrentTrie<>(new StrategyFollowingArrayTrie<T>(ArrayTrieStrategy.SINGLE_CASE_NUMERIC));
    }

    public static <T> Trie<T> newConcurrentMixedCaseArrayTrie() {
        return new VeryConcurrentTrie<>(new StrategyFollowingArrayTrie<T>(ArrayTrieStrategy.MIXED_CASE));
    }

    public static <T> Trie<T> newConcurrentMixedCaseNumericArrayTrie() {
        return new VeryConcurrentTrie<>(new StrategyFollowingArrayTrie<T>(ArrayTrieStrategy.MIXED_CASE_NUMERIC));
    }

//    public static <T> Trie<T> newHashTrie() {
//        return new AbstractTrie<T>() {
//            @Override
//            protected TrieNode<T> newTrieNode(T value) {
//                return new MapTrieNode<>(Maps.<Character, TrieNode<T>>newHashMap(), value);
//            }
//        };
//    }
//
//    public static <T> Trie<T> newConcurrentHashTrie() {
//        return new ConcurrentTrie<>(new AbstractTrie<T>() {
//            @Override
//            protected TrieNode<T> newTrieNode(T value) {
//                return new MapTrieNode<>(Maps.<Character, TrieNode<T>>newConcurrentMap(), value);
//            }
//        });
//    }

    private static class ConcurrentTrie<T> extends AbstractTrie<T> {

        protected final AbstractTrie<T> delegate;

        protected final ReadWriteLock rootLock;
        protected final ReadWriteLock sizeLock;

        private ConcurrentTrie(AbstractTrie<T> delegate, ReadWriteLock rootLock, ReadWriteLock sizeLock) {
            this.delegate = delegate;
            this.rootLock = rootLock;
            this.sizeLock = sizeLock;
        }

        private ConcurrentTrie(AbstractTrie<T> delegate) {
            this(delegate, new ReentrantReadWriteLock(), new ReentrantReadWriteLock());
        }

        @Override
        protected TrieNode<T> newTrieNode(T value) {
            return delegate.newTrieNode(value);
        }

        @Override
        protected void modifySize(int delta) {
            sizeLock.writeLock().lock();
            try {
                delegate.modifySize(delta);
            } finally {
                sizeLock.writeLock().unlock();
            }
        }

        @Override
        public int size() {
            sizeLock.readLock().lock();
            try {
                return delegate.size();
            } finally {
                sizeLock.readLock().unlock();
            }
        }

        @Override
        public void clear() {
            rootLock.writeLock().lock();
            sizeLock.writeLock().lock();
            try {
                delegate.clear();
            } finally {
                rootLock.writeLock().unlock();
                sizeLock.writeLock().unlock();
            }
        }

        @Override
        protected void setRootIfNull() {
            rootLock.readLock().lock();
            try {
                if (delegate.getRoot() == null) {
                    rootLock.readLock().unlock();
                    rootLock.writeLock().lock();
                    try {
                        if (delegate.getRoot() == null) {
                            delegate.setRootIfNull();
                        }
                    } finally {
                        rootLock.readLock().lock();
                        rootLock.writeLock().unlock();
                    }
                }
            } finally {
                rootLock.readLock().unlock();
            }
        }

        @Override
        protected TrieNode<T> getRoot() {
            rootLock.readLock().lock();
            try {
                return delegate.getRoot();
            } finally {
                rootLock.readLock().unlock();
            }
        }

        @Override
        public Trie<T> getSubTrie(String key) {
            return new ConcurrentTrie<>((AbstractTrie<T>) delegate.getSubTrie(key), rootLock, sizeLock);
        }

        @Override
        public Trie<T> removeSubTrie(String key) {
            return new ConcurrentTrie<>((AbstractTrie<T>) delegate.getSubTrie(key), rootLock, sizeLock);
        }
    }

    private static class VeryConcurrentTrie<T> extends ConcurrentTrie<T> {

        private VeryConcurrentTrie(AbstractTrie<T> delegate, ReadWriteLock rootLock, ReadWriteLock sizeLock) {
            super(delegate, rootLock, sizeLock);
        }

        private VeryConcurrentTrie(AbstractTrie<T> delegate) {
            super(delegate);
        }

        @Override
        protected TrieNode<T> newTrieNode(T value) {
            return concurrentTrieNode(delegate.newTrieNode(value));
        }

        @Override
        public Trie<T> getSubTrie(String key) {
            return new VeryConcurrentTrie<>((AbstractTrie<T>) delegate.getSubTrie(key), rootLock, sizeLock);
        }

        @Override
        public Trie<T> removeSubTrie(String key) {
            return new VeryConcurrentTrie<>((AbstractTrie<T>) delegate.getSubTrie(key), rootLock, sizeLock);
        }

    }

    private static <V> TrieNode<V> concurrentTrieNode(final TrieNode<V> node) {
        return new TrieNode<V>() {

            private final ReadWriteLock lock = new ReentrantReadWriteLock();

            @Override
            public V getValue() {
                lock.readLock().lock();
                try {
                    return node.getValue();
                } finally {
                    lock.readLock().unlock();
                }
            }

            @Override
            public V setValue(V value) {
                lock.writeLock().lock();
                try {
                    return node.setValue(value);
                } finally {
                    lock.writeLock().unlock();
                }
            }

            @Override
            public TrieNode<V> getChild(char c) {
                lock.readLock().lock();
                try {
                    return node.getChild(c);
                } finally {
                    lock.readLock().unlock();
                }
            }

            @Override
            public void setChild(char c, TrieNode<V> child) {
                lock.writeLock().lock();
                try {
                    node.setChild(c, child);
                } finally {
                    lock.writeLock().unlock();
                }
            }

            @Override
            public boolean isEmpty() {
                lock.readLock().lock();
                try {
                    return node.isEmpty();
                } finally {
                    lock.readLock().unlock();
                }
            }

            @Override
            public void clear() {
                lock.writeLock().lock();
                try {
                    node.clear();
                } finally {
                    lock.writeLock().unlock();
                }
            }

            @Override
            public PeekingIterator<CharacterAndNode<V>> iterator() {
                lock.readLock().lock();
                try {
                    return MoreIterators.lockedPeekingIterator(node.iterator(), lock);
                } finally {
                    lock.readLock().unlock();
                }
            }

            @Override
            public PeekingIterator<CharacterAndNode<V>> iterator(char c) {
                lock.readLock().lock();
                try {
                    return MoreIterators.lockedPeekingIterator(node.iterator(c), lock);
                } finally {
                    lock.readLock().unlock();
                }
            }

            @Override
            public PeekingIterator<CharacterAndNode<V>> reverseIterator() {
                lock.readLock().lock();
                try {
                    return MoreIterators.lockedPeekingIterator(node.reverseIterator(), lock);
                } finally {
                    lock.readLock().unlock();
                }
            }

            @Override
            public PeekingIterator<CharacterAndNode<V>> reverseIterator(char c) {
                lock.readLock().lock();
                try {
                    return MoreIterators.lockedPeekingIterator(node.reverseIterator(c), lock);
                } finally {
                    lock.readLock().unlock();
                }
            }
        };
    }

    private static class StrategyFollowingArrayTrie<V> extends AbstractTrie<V> {
        private final ArrayTrieStrategy strategy;

        public StrategyFollowingArrayTrie(ArrayTrieStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        protected TrieNode<V> newTrieNode(V value) {
            return new AbstractArrayTrieNode<V>(value) {
                @Override
                protected int charToIndex(char c) {
                    return strategy.charToIndex(c);
                }

                @Override
                protected char indexToChar(int i) {
                    return strategy.indexToChar(i);
                }

                @Override
                protected int charsetSize() {
                    return strategy.charsetSize();
                }
            };
        }

        @Override
        public boolean isCharacterInteresting(char c) {
            return strategy.isCharacterInteresting(c);
        }
    }

    private static class StrategyFollowingArrayTrieNode<V> extends AbstractArrayTrieNode<V> {
        private final ArrayTrieStrategy strategy;

        public StrategyFollowingArrayTrieNode(ArrayTrieStrategy strategy, V val) {
            super(val);
            this.strategy = strategy;
        }

        @Override
        protected int charToIndex(char c) {
            return strategy.charToIndex(c);
        }

        @Override
        protected char indexToChar(int i) {
            return strategy.indexToChar(i);
        }

        @Override
        protected int charsetSize() {
            return strategy.charsetSize();
        }
    }


    public static <T> Trie<T> unmodifiableTrie(final Trie<T> trie) {
        return new ForwardingTrie<T>() {
            @Override
            protected Trie<T> delegate() {
                return trie;
            }

            @Override
            public Trie<T> getSubTrie(String key) {
                return unmodifiableTrie(trie.getSubTrie(key));
            }

            @Override
            public Trie<T> removeSubTrie(String key) {
                throw new UnsupportedOperationException();
            }

            @Override
            public T put(String key, T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public T remove(Object key) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void putAll(Map<? extends String, ? extends T> m) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void clear() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static class EmptyTrie implements Trie<Object> {
        private static final Trie<Object> INSTANCE = new EmptyTrie();

        @Override
        public Trie<Object> getSubTrie(String key) {
            return this;
        }

        @Override
        public Trie<Object> removeSubTrie(String key) {
            return this;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Object get(Object key) {
            return null;
        }

        @Override
        public Object put(String key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> m) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
        }

        @Override
        public Comparator<? super String> comparator() {
            return null;
        }

        @Override
        public Entry<String, Object> lowerEntry(String key) {
            return null;
        }

        @Override
        public String lowerKey(String key) {
            return null;
        }

        @Override
        public Entry<String, Object> floorEntry(String key) {
            return null;
        }

        @Override
        public String floorKey(String key) {
            return null;
        }

        @Override
        public Entry<String, Object> ceilingEntry(String key) {
            return null;
        }

        @Override
        public String ceilingKey(String key) {
            return null;
        }

        @Override
        public Entry<String, Object> higherEntry(String key) {
            return null;
        }

        @Override
        public String higherKey(String key) {
            return null;
        }

        @Override
        public Entry<String, Object> firstEntry() {
            return null;
        }

        @Override
        public Entry<String, Object> lastEntry() {
            return null;
        }

        @Override
        public Entry<String, Object> pollFirstEntry() {
            return null;
        }

        @Override
        public Entry<String, Object> pollLastEntry() {
            return null;
        }

        @Override
        public Trie<Object> descendingMap() {
            return this;
        }

        @Override
        public NavigableSet<String> navigableKeySet() {
            return null;// TODO: fix this?
        }

        @Override
        public NavigableSet<String> descendingKeySet() {
            return null;// TODO: fix this?
        }

        @Override
        public Trie<Object> subMap(String fromKey, boolean fromInclusive, String toKey, boolean toInclusive) {
            return this;
        }

        @Override
        public Trie<Object> headMap(String toKey, boolean inclusive) {
            return this;
        }

        @Override
        public Trie<Object> tailMap(String fromKey, boolean inclusive) {
            return this;
        }

        @Override
        public SortedMap<String, Object> subMap(String fromKey, String toKey) {
            return this;
        }

        @Override
        public SortedMap<String, Object> headMap(String toKey) {
            return this;
        }

        @Override
        public SortedMap<String, Object> tailMap(String fromKey) {
            return this;
        }

        @Override
        public String firstKey() {
            throw new NoSuchElementException();
        }

        @Override
        public String lastKey() {
            throw new NoSuchElementException();
        }

        @Override
        public Set<String> keySet() {
            return Collections.emptySet();
        }

        @Override
        public Collection<Object> values() {
            return Collections.emptySet();
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return Collections.emptySet();
        }
    }
}
