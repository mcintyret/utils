package com.mcintyret.utils.collect;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * User: mcintyret2
 * Date: 02/04/2013
 */
public final class MoreCollections {

    private MoreCollections() {

    }

    public static <T, C extends Collection<? extends T>> C largest(C... colls) {
        checkArgument(colls.length > 0);
        C largest = null;
        for (C c : colls) {
            if (largest == null || c.size() > largest.size()) {
                largest = c;
            }
        }
        return largest;
    }

    public static <T, C extends Collection<? extends T>> C smallest(C... colls) {
        checkArgument(colls.length > 0);
        C smallest = null;
        for (C c : colls) {
            if (smallest == null || c.size() < smallest.size()) {
                smallest = c;
            }
        }
        return smallest;
    }

    public static <T> Collection<T> synchronizedCollection(Collection<T> c) {
        return new SynchronizedCollection<>(c);
    }

    public static <T> Set<T> synchronizedSet(Set<T> set) {
        return new SynchronizedSet<>(set);
    }

    public static <T> List<T> synchronizedList(List<T> list) {
        return list instanceof RandomAccess ? new RandomAccessSynchronizedList<>(list) : new SynchronizedList<>(list);
    }

    public static <K, V> Map<K, V> synchronizedMap(Map<K, V> map) {
        return new SynchronizedMap<>(map);
    }

    public static <V> Trie<V> synchronizedTrie(Trie<V> trie) {
        return new SynchronizedTrie<>(trie);
    }

    private static class SynchronizedCollection<E> implements Collection<E> {

        private final Collection<E> delegate;
        protected final ReadWriteLock lock;

        private SynchronizedCollection(Collection<E> delegate, ReadWriteLock lock) {
            this.delegate = delegate;
            this.lock = lock;
        }

        private SynchronizedCollection(Collection<E> delegate) {
            this(delegate, new ReentrantReadWriteLock());
        }


        @Override
        public int size() {
            lock.readLock().lock();
            try {
                return delegate.size();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isEmpty() {
            lock.readLock().lock();
            try {
                return delegate.isEmpty();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean contains(Object o) {
            lock.readLock().lock();
            try {
                return delegate.contains(o);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public Iterator<E> iterator() {
            return MoreIterators.lockedIterator(delegate.iterator(), lock);
        }

        @Override
        public Object[] toArray() {
            lock.readLock().lock();
            try {
                return delegate.toArray();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public <T> T[] toArray(T[] a) {
            lock.readLock().lock();
            try {
                return delegate.toArray(a);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean add(E e) {
            lock.writeLock().lock();
            try {
                return delegate.add(e);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public boolean remove(Object o) {
            lock.writeLock().lock();
            try {
                return delegate.remove(o);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            lock.readLock().lock();
            try {
                return delegate.containsAll(c);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            lock.writeLock().lock();
            try {
                return delegate.addAll(c);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            lock.writeLock().lock();
            try {
                return delegate.removeAll(c);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            lock.writeLock().lock();
            try {
                return delegate.retainAll(c);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void clear() {
            lock.writeLock().lock();
            try {
                delegate.clear();
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    private static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {

        private final Set<E> delegate;

        private SynchronizedSet(Set<E> delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        private SynchronizedSet(Set<E> delegate, ReadWriteLock lock) {
            super(delegate, lock);
            this.delegate = delegate;
        }

        @Override
        public boolean equals(Object o) {
            lock.readLock().lock();
            try {
                return delegate.equals(o);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public int hashCode() {
            lock.readLock().lock();
            try {
                return delegate.hashCode();
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {

        protected final List<E> delegate;

        private SynchronizedList(List<E> delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        private SynchronizedList(List<E> delegate, ReadWriteLock lock) {
            super(delegate, lock);
            this.delegate = delegate;
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            lock.writeLock().lock();
            try {
                return delegate.addAll(index, c);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public E get(int index) {
            lock.readLock().lock();
            try {
                return delegate.get(index);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public E set(int index, E element) {
            lock.writeLock().lock();
            try {
                return delegate.set(index, element);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void add(int index, E element) {
            lock.writeLock().lock();
            try {
                delegate.add(index, element);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public E remove(int index) {
            lock.writeLock().lock();
            try {
                return delegate.remove(index);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public int indexOf(Object o) {
            lock.readLock().lock();
            try {
                return delegate.indexOf(o);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public int lastIndexOf(Object o) {
            lock.readLock().lock();
            try {
                return delegate.lastIndexOf(o);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public ListIterator<E> listIterator() {
            return MoreIterators.lockedListIterator(delegate.listIterator(), lock);
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            return MoreIterators.lockedListIterator(delegate.listIterator(index), lock);
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return new SynchronizedList<>(delegate.subList(fromIndex, toIndex), lock);
        }
    }

    private static class RandomAccessSynchronizedList<E> extends SynchronizedList<E> implements RandomAccess {

        private RandomAccessSynchronizedList(List<E> delegate) {
            super(delegate);
        }

        private RandomAccessSynchronizedList(List<E> delegate, ReadWriteLock lock) {
            super(delegate, lock);
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return new RandomAccessSynchronizedList<>(delegate.subList(fromIndex, toIndex), lock);
        }

    }

    private static class SynchronizedMap<K, V> implements Map<K, V> {

        private final Map<K, V> delegate;
        protected final ReadWriteLock lock;

        private SynchronizedMap(Map<K, V> delegate, ReadWriteLock lock) {
            this.delegate = delegate;
            this.lock = lock;
        }

        private SynchronizedMap(Map<K, V> delegate) {
            this(delegate, new ReentrantReadWriteLock());
        }

        @Override
        public int size() {
            lock.readLock().lock();
            try {
                return delegate.size();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isEmpty() {
            lock.readLock().lock();
            try {
                return delegate.isEmpty();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean containsKey(Object key) {
            lock.readLock().lock();
            try {
                return delegate.containsKey(key);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean containsValue(Object value) {
            lock.readLock().lock();
            try {
                return delegate.containsValue(value);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public V get(Object key) {
            lock.readLock().lock();
            try {
                return delegate.get(key);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public V put(K key, V value) {
            lock.writeLock().lock();
            try {
                return delegate.put(key, value);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public V remove(Object key) {
            lock.writeLock().lock();
            try {
                return delegate.remove(key);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            lock.writeLock().lock();
            try {
                delegate.putAll(m);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void clear() {
            lock.writeLock().lock();
            try {
                delegate.clear();
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public Set<K> keySet() {
            return new SynchronizedSet<>(delegate.keySet(), lock);
        }

        @Override
        public Collection<V> values() {
            return new SynchronizedCollection<>(delegate.values(), lock);
        }

        @Override
        public Set<Entry<K, V>> entrySet() {
            return new SynchronizedSet<>(delegate.entrySet(), lock);
        }
    }

    private static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V> {

        private final SortedMap<K, V> delegate;

        private SynchronizedSortedMap(SortedMap<K, V> delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        private SynchronizedSortedMap(SortedMap<K, V> delegate, ReadWriteLock lock) {
            super(delegate, lock);
            this.delegate = delegate;
        }

        @Override
        public Comparator<? super K> comparator() {
            return delegate.comparator(); // Doesn't require locking
        }

        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            lock.readLock().lock();
            try {
                return new SynchronizedSortedMap<>(delegate.subMap(fromKey, toKey), lock);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public SortedMap<K, V> headMap(K toKey) {
            lock.readLock().lock();
            try {
                return new SynchronizedSortedMap<>(delegate.headMap(toKey), lock);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            lock.readLock().lock();
            try {
                return new SynchronizedSortedMap<>(delegate.tailMap(fromKey), lock);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public K firstKey() {
            lock.readLock().lock();
            try {
                return delegate.firstKey();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public K lastKey() {
            lock.readLock().lock();
            try {
                return delegate.lastKey();
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    private static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> {

        private final NavigableMap<K, V> delegate;

        protected SynchronizedNavigableMap(NavigableMap<K, V> delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        protected SynchronizedNavigableMap(NavigableMap<K, V> delegate, ReadWriteLock lock) {
            super(delegate, lock);
            this.delegate = delegate;
        }

        @Override
        public Entry<K, V> lowerEntry(K key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public K lowerKey(K key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Entry<K, V> floorEntry(K key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public K floorKey(K key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Entry<K, V> ceilingEntry(K key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public K ceilingKey(K key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Entry<K, V> higherEntry(K key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public K higherKey(K key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Entry<K, V> firstEntry() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Entry<K, V> lastEntry() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Entry<K, V> pollFirstEntry() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Entry<K, V> pollLastEntry() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public NavigableSet<K> navigableKeySet() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public NavigableSet<K> descendingKeySet() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private static final class SynchronizedTrie<V> extends SynchronizedNavigableMap<String, V> implements Trie<V> {

        private final Trie<V> delegate;

        private SynchronizedTrie(Trie<V> delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        private SynchronizedTrie(Trie<V> delegate, ReadWriteLock lock) {
            super(delegate, lock);
            this.delegate = delegate;
        }

        @Override
        public Trie<V> getSubTrie(String key) {
            return new SynchronizedTrie<>(delegate.getSubTrie(key), lock);
        }

        @Override
        public Trie<V> removeSubTrie(String key) {
            return new SynchronizedTrie<>(delegate.removeSubTrie(key), lock);
        }

        @Override
        public Trie<V> subMap(String from, boolean fromInclusive, String to, boolean toInclusive) {
            return new SynchronizedTrie<>(delegate.subMap(from, fromInclusive, to, toInclusive), lock);
        }

        @Override
        public Trie<V> headMap(String to, boolean toInclusive) {
            return new SynchronizedTrie<>(delegate.headMap(to, toInclusive), lock);
        }

        @Override
        public Trie<V> tailMap(String from, boolean fromInclusive) {
            return new SynchronizedTrie<>(delegate.tailMap(from, fromInclusive), lock);
        }

        @Override
        public Trie<V> descendingMap() {
            return new SynchronizedTrie<>(delegate.descendingMap(), lock);
        }
    }
}
