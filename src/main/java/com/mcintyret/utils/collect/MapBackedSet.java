package com.mcintyret.utils.collect;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

/**
 * User: mcintyret2
 * Date: 06/03/2013
 */
public class MapBackedSet<T> extends AbstractSet<T> {

    private static final Object IS_PRESENT = new Object();

    private final Map<T, Object> map;

    public MapBackedSet(Map<T, Object> map) {
        this.map = map;
    }

    @Override
    public boolean add(T t) {
        return (map.put(t, IS_PRESENT) == null);
    }

    @Override
    public boolean contains(Object o) {
        return map.get(o) == IS_PRESENT;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == IS_PRESENT;
    }

    @Override
    public Iterator<T> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }
}
