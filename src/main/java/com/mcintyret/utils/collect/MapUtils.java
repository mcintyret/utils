package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;
import com.mcintyret.utils.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * User: mcintyret2
 * Date: 09/04/2013
 */
public class MapUtils {

    public static <K, V> K keyOrNull(Map.Entry<K, V> entry) {
        return entry != null ? entry.getKey() : null;
    }

    public static <K, V> Collection<V> values(final Map<K, V> map) {
        return new AbstractCollection<V>() {

            @Override
            public Iterator<V> iterator() {
                return Iterators.transform(map.entrySet().iterator(), Utils.<K, V>valueFunction());
            }

            @Override
            public int size() {
                return map.size();
            }
        };
    }

    public static <K, V> Set<K> keySet(final Map<K, V> map) {
        return new AbstractSet<K>() {
            @Override
            public Iterator<K> iterator() {
                return Iterators.transform(map.entrySet().iterator(), Utils.<K, V>keyFunction());
            }

            @Override
            public int size() {
                return map.size();
            }

            @Override
            public boolean remove(Object o) {
                return map.remove(o) != null;
            }
        };
    }

    public static boolean deepEquals(Map<?, ?> one, Map<?, ?> two) {
        if (one.size() != two.size()) {
            return false;
        }
        for (Map.Entry<?, ?> entry : one.entrySet()) {
            if (!Objects.deepEquals(entry.getValue(), two.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

}
