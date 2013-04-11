package com.mcintyret.utils.collect;

import com.google.common.base.Function;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

/**
 * User: mcintyret2
 * Date: 10/04/2013
 */
public class MapSetView<K> extends AbstractSet<K> {

    private final Map<K, ?> map;

    public MapSetView(Map<K, ?> map) {
        this.map = map;
    }

    @Override
    public Iterator<K> iterator() {
        return MoreIterators.transform(map.entrySet().iterator(), keyFunction());
    }

    private Function<Map.Entry<K, ?>, K> keyFunction() {
        return new Function<Map.Entry<K, ?>, K>() {
            @Override
            public K apply(Map.Entry<K, ?> input) {
                return input.getKey();
            }
        };
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean remove(Object key) {
        if (map.containsKey(key)) {
            map.remove(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean contains(Object key) {
        return map.containsKey(key);
    }
}
