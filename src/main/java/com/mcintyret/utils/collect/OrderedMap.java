package com.mcintyret.utils.collect;

import java.util.Map;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 */
public interface OrderedMap<K, V> extends Map<K, V> {

    Entry<K, V> get(int i);

    Entry<K, V> remove(int i);

    OrderedMap<K, V> submap(int from, int to);

}
