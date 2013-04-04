package com.mcintyret.utils.collect;


import com.google.common.base.Function;
import com.google.common.collect.Maps;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * User: mcintyret2
 * Date: 02/04/2013
 */
public final class ComputableKeyMaps {

    private ComputableKeyMaps() {

    }

    public static <K, V> Collection<V> toCollection(Function<V, K> keyFromValueFunction, Map<K, V> map) {
        return new ComputableKeyMap<>(keyFromValueFunction, map);
    }

    public static <K, V> TreeMap<K, V> toTreeMap(Function<V, K> keyFromValueFunction, Iterable<V> iterable) {
        return toMap(keyFromValueFunction, iterable, new TreeMap<K, V>());
    }

    public static <K, V> TreeMap<K, V> toTreeMap(Function<V, K> keyFromValueFunction, V... array) {
        return toMap(keyFromValueFunction, Arrays.asList(array), new TreeMap<K, V>());
    }

    public static <K, V> HashMap<K, V> toHashMap(Function<V, K> keyFromValueFunction, V... array) {
        return toMap(keyFromValueFunction, Arrays.asList(array), Maps.<K, V>newHashMapWithExpectedSize(array.length));
    }

    public static <K, V> HashMap<K, V> toHashMap(Function<V, K> keyFromValueFunction, Iterable<V> iterable) {
        HashMap<K, V> map = iterable instanceof Collection ? Maps.<K, V>newHashMapWithExpectedSize(((Collection) iterable).size()) : Maps.<K, V>newHashMap();
        return toMap(keyFromValueFunction, iterable, map);
    }

    public static <K, V, M extends Map<K, V>> M toMap(Function<V, ? extends K> keyFromValueFunction, Iterable<V> iterable, M emptyMap) {
        checkArgument(emptyMap.isEmpty());
        for (V v : iterable) {
            emptyMap.put(keyFromValueFunction.apply(v), v);
        }
        return emptyMap;
    }


}
