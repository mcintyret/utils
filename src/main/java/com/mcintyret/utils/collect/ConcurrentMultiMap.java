package com.mcintyret.utils.collect;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

/**
 * User: mcintyret2
 * Date: 18/08/2013
 */
public class ConcurrentMultiMap<K, V> implements Multimap<K, V> {

    private final ConcurrentMap<K, Collection<V>> map = new ConcurrentHashMap<>();

    @Override
    public int size() {
        int size = 0;
        for (Collection<V> c : map.values()) {
            size += c.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return map.get(key) != null;
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        for (Collection<V> c : map.values()) {
            if (c.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
        Collection<V> col = map.get(key);
        return col != null && col.contains(value);
    }

    @Override
    public boolean put(K key, V value) {
        return getOrPutCol(key).add(value);
    }

    private Collection<V> getOrPutCol(K key) {
        return map.computeIfAbsent(key, k -> loader.load());
    }

    @Override
    public boolean remove(Object key, Object value) {
        Collection<V> col = map.get(key);
        return col != null && col.remove(value);
    }

    @Override
    public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
        return Iterables.addAll(getOrPutCol(key), values);
    }

    @Override
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        boolean changed = false;
        for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
            changed |= putAll(entry.getKey(), entry.getValue());
        }
        return changed;
    }

    @Override
    public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
        Collection<V> vals = new ConcurrentLinkedDeque<>();
        Iterables.addAll(vals, values);
        return nullToEmpty(map.put(key, vals));
    }

    @Override
    public Collection<V> removeAll(Object key) {
        return nullToEmpty(map.remove(key));
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Collection<V> get(K key) {
        return nullToEmpty(map.get(key));
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Multiset<K> keys() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<V> values() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<Map.Entry<K, V>> entries() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<K, Collection<V>> asMap() {
        return map;
    }

    private Collection<V> nullToEmpty(Collection<V> in) {
        return in == null ? Collections.<V>emptyList() : in;
    }

    private final Loader<Collection<V>> loader = new Loader<Collection<V>>() {
        @Override
        public Collection<V> load() {
            return new ConcurrentLinkedDeque<>();
        }
    };
}
