package com.mcintyret.utils.collect;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

import javax.annotation.Nullable;
import java.util.*;

/**
 * User: tommcintyre
 * Date: 8/21/13
 */
public abstract class AbstractEnumMultimap<E extends Enum<E>, V, C extends Collection<V>> implements Multimap<E, V> {

    private int totalSize = 0;

    private final Map<E, C> map;

    public AbstractEnumMultimap(Class<E> enumClass) {
        this.map = new EnumMap<>(enumClass);
    }

    @Override
    public int size() {
        return totalSize;
    }

    @Override
    public boolean isEmpty() {
        return totalSize == 0;
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return map.get(key) != null;
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
        C c = map.get(key);
        return c != null && c.contains(value);
    }

    @Override
    public boolean put(@Nullable E key, @Nullable V value) {
        boolean added = getOrCreate(key).add(value);
        if (added) {
            totalSize++;
        }
        return added;
    }

    private C getOrCreate(E key) {
        C col = map.get(key);
        if (col == null) {
            col = newCollection();
            map.put(key, col);
        }
        return col;
    }

    @Override
    public boolean remove(@Nullable Object key, @Nullable Object value) {
        C c = map.get(key);
        boolean removed = c != null && c.remove(value);
        if (removed) {
            totalSize--;
        }
        return removed;
    }

    @Override
    public boolean putAll(@Nullable E key, Iterable<? extends V> values) {
        C col = getOrCreate(key);
        int oldSize = col.size();
        Iterables.addAll(col, values);
        int newSize = col.size();
        totalSize += newSize - oldSize;
        return newSize != oldSize;
    }

    @Override
    public boolean putAll(Multimap<? extends E, ? extends V> multimap) {
        boolean changed = false;
        for (Map.Entry<? extends E, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
            changed |= putAll(entry.getKey(), entry.getValue());
        }
        return changed;
    }

    @Override
    public Collection<V> replaceValues(@Nullable E key, Iterable<? extends V> values) {
        C before = getOrEmpty(key);
        C after = newCollection();
        Iterables.addAll(after, values);
        totalSize += (after.size() - before.size());
        map.put(key, after);
        return before;
    }

    @Override
    public Collection<V> removeAll(@Nullable Object key) {
        C c = getOrEmpty(key);
        map.remove(key);
        totalSize -= c.size();
        return c;
    }

    private C getOrEmpty(@Nullable Object key) {
        C col = map.get(key);
        return col == null ? emptyCollection() : col;
    }

    @Override
    public void clear() {
        map.clear();
        totalSize = 0;
    }

    @Override
    public Collection<V> get(@Nullable E key) {
        return getOrEmpty(key);
    }

    @Override
    public Set<E> keySet() {
        return map.keySet();
    }

    @Override
    public Multiset<E> keys() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<V> values() {
        return new AbstractCollection<V>() {
            @Override
            public Iterator<V> iterator() {
                return Iterators.transform(entries().iterator(), new Function<Map.Entry<E, V>, V>() {
                    @Nullable
                    @Override
                    public V apply(@Nullable Map.Entry<E, V> input) {
                        return input.getValue();
                    }
                });
            }

            @Override
            public int size() {
                return totalSize;
            }
        };
    }

    @Override
    public Collection<Map.Entry<E, V>> entries() {
        return new AbstractCollection<Map.Entry<E, V>>() {

            @Override
            public Iterator<Map.Entry<E, V>> iterator() {
                return new AbstractIterator<Map.Entry<E, V>>() {

                    private Iterator<E> keyIterator = map.keySet().iterator();
                    private E key = keyIterator.hasNext() ? keyIterator.next() : null;
                    private Iterator<V> valueIterator = key == null ? map.get(key).iterator() : Iterators.<V>emptyIterator();

                    @Override
                    protected Map.Entry<E, V> computeNext() {
                        if (valueIterator.hasNext()) {
                            return new Entry<>(key, valueIterator.next());
                        } else {
                            if (keyIterator.hasNext()) {
                                key = keyIterator.next();
                                valueIterator = map.get(key).iterator();
                                return computeNext();
                            } else {
                                return endOfData();
                            }
                        }
                    }

                    @Override
                    protected void doRemove(Map.Entry<E, V> removed) {
                        AbstractEnumMultimap.this.remove(removed.getKey(), removed.getValue());
                    }
                };
            }

            @Override
            public int size() {
                return totalSize;
            }

            @Override
            public boolean remove(Object o) {
                if (o instanceof Map.Entry) {
                    Map.Entry<E, V> entry = (Map.Entry<E, V>) o;
                    return AbstractEnumMultimap.this.remove(entry.getKey(), entry.getValue());
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    public Map<E, Collection<V>> asMap() {
        return (Map<E, Collection<V>>) map;
    }

    protected abstract C newCollection();

    protected abstract C emptyCollection();


    private static class Entry<E, V> implements Map.Entry<E, V> {
        private final E key;

        private final V val;

        private Entry(E key, V val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public E getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return val;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;

            Map.Entry entry = (Map.Entry) o;

            if (!key.equals(entry.getKey())) return false;
            if (val != null ? !val.equals(entry.getValue()) : entry.getValue() != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + (val != null ? val.hashCode() : 0);
            return result;
        }
    }

}
