package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;
import com.mcintyret.utils.Utils;

import java.util.*;

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

    public static <V> Map.Entry<String, V> trieNodeEntry(final TrieNode<V> node, final String key) {
        return new Map.Entry<String, V>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return node.getValue();
            }

            @Override
            public V setValue(V value) {
                return node.setValue(value);
            }
        };
    }
}
