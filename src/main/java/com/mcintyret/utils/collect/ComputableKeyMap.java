package com.mcintyret.utils.collect;

import com.google.common.base.Function;

import java.util.*;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public class ComputableKeyMap<K, V> extends AbstractCollection<V> {

    private final Function<V, K> keyFromValueFunction;

    private final Map<K, V> map;

    protected ComputableKeyMap(Function<V, K> keyFromValueFunction, Map<K, V> map) {
        this.keyFromValueFunction = keyFromValueFunction;
        this.map = map;
    }

    @Override
    public boolean contains(Object o) {
        V v = map.get(toKey(o));
        return Objects.equals(v, o);
    }

    @Override
    public boolean remove(Object o) {
        K key = toKey(o);
        V existing = map.get(key);
        if (Objects.equals(o, existing)) {
            map.remove(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Iterator<V> iterator() {
        return map.values().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Object[] toArray() {
        return map.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return map.values().toArray(a);
    }

    @Override
    public boolean add(V v) {
        V existing = map.put(toKey(v), v);
        return !Objects.equals(v, existing);
    }


    private K toKey(Object o) {
        return keyFromValueFunction.apply((V) o);
    }
}

