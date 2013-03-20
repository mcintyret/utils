package com.mcintyret.utils.collect;

import com.google.common.collect.ForwardingMap;

import java.util.*;

import static com.google.common.base.Preconditions.*;
import static com.mcintyret.utils.Utils.keyIterator;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 */
public class OrderedMapAdapter<K, V> extends ForwardingMap<K, V> implements OrderedMap<K, V> {

    private static final int DEFAULT_LIST_SIZE = 10;

    private final Map<K, V> delegate;

    private final List<Entry<K, V>> entryList;

    public OrderedMapAdapter(Map<K, V> delegate) {
        checkArgument(delegate.isEmpty(), "Must create an OrderedMapAdapter with a fresh Map!");
        this.delegate = delegate;
        entryList = new EntryList();
    }

    @Override
    protected Map<K, V> delegate() {
        return delegate;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V put(K key, V value) {
        V existing = delegate().put(key, value);
        if (existing == null) {
            // simple case - add this as the last entry
            entryList.add(new SimpleEntry<>(key, value));
            return null;
        } else {
            for (Entry<K, V> entry : entryList) {
                if (Objects.equals(key, entry.getKey()) &&
                        Objects.equals(existing, entry.getValue())) {
                    entry.setValue(value);
                    return existing;
                }
            }
            throw new IllegalStateException("Cannot find previous mapping");
        }
    }

    private Set<Entry<K, V>> entrySet;

    @Override
    public Set<Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<Entry<K, V>>() {

                @Override
                public boolean add(Entry<K, V> entry) {
                    V existing = OrderedMapAdapter.this.put(entry.getKey(), entry.getValue());
                    return !Objects.equals(existing, entry.getValue());
                }

                @Override
                public boolean contains(Object o) {
                    return OrderedMapAdapter.this.containsKey(((Entry<K, V>) o).getKey());
                }

                @Override
                public Iterator<Entry<K, V>> iterator() {
                    return entryList.iterator();
                }

                @Override
                public int size() {
                    return OrderedMapAdapter.this.size();
                }
            };
        }
        return entrySet;
    }

    private Set<K> keySet;

    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new AbstractSet<K>() {

                @Override
                public boolean contains(Object o) {
                    return OrderedMapAdapter.this.containsKey(o);
                }

                @Override
                public Iterator<K> iterator() {
                    return keyIterator(entryList.iterator());
                }

                @Override
                public int size() {
                    return OrderedMapAdapter.this.size();
                }
            };
        }
        return keySet;
    }

    @Override
    public Entry<K, V> get(int i) {
        checkPositionIndex(i, size());
        return entryList.get(i);
    }

    @Override
    public Entry<K, V> remove(int i) {
        checkPositionIndex(i, size());
        Entry<K, V> entry = entryList.remove(i);
        if (delegate().remove(entry.getKey()) != entry.getValue()) {
            throw new IllegalStateException();
        }
        return entry;
    }

    @Override
    public OrderedMap<K, V> submap(int from, int to) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private class EntryList extends ArrayList<Entry<K, V>> {

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new Iterator<Entry<K, V>>() {

                private final Iterator<Entry<K, V>> superIterator = EntryList.super.iterator();
                private Entry<K, V> last;

                @Override
                public boolean hasNext() {
                    return superIterator.hasNext();
                }

                @Override
                public Entry<K, V> next() {
                    last = superIterator.next();
                    return last;
                }

                @Override
                public void remove() {
                    checkState(last != null);
                    superIterator.remove();
                    OrderedMapAdapter.this.remove(last.getKey());
                    last = null;
                }
            };
        }

    }

    private static class SimpleEntry<K, V> implements Entry<K, V> {

        private final K key;
        private V value;

        private SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldVal = this.value;
            this.value = value;
            return oldVal;
        }
    }
}
