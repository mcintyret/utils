package com.mcintyret.utils.collect;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;

/**
 * User: mcintyret2
 * Date: 10/04/2013
 */
public class NavigableMapSetView<K> extends SortedMapSetView<K> implements NavigableSet<K> {

    private final NavigableMap<K, ?> map;

    public NavigableMapSetView(NavigableMap<K, ?> map) {
        super(map);
        this.map = map;
    }

    @Override
    public K lower(K k) {
        return map.lowerKey(k);
    }

    @Override
    public K floor(K k) {
        return map.floorKey(k);
    }

    @Override
    public K ceiling(K k) {
        return map.ceilingKey(k);
    }

    @Override
    public K higher(K k) {
        return map.higherKey(k

        );
    }

    @Override
    public K pollFirst() {
        return keyOrNull(map.pollFirstEntry());
    }

    @Override
    public K pollLast() {
        return keyOrNull(map.pollLastEntry());
    }

    @Override
    public NavigableSet<K> descendingSet() {
        return new NavigableMapSetView<>(map.descendingMap());
    }

    @Override
    public Iterator<K> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
        return new NavigableMapSetView<>(map.subMap(fromElement, fromInclusive, toElement, toInclusive));
    }

    @Override
    public NavigableSet<K> headSet(K toElement, boolean inclusive) {
        return new NavigableMapSetView<>(map.headMap(toElement, inclusive));
    }

    @Override
    public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
        return new NavigableMapSetView<>(map.tailMap(fromElement, inclusive));
    }

    private static <K> K keyOrNull(Map.Entry<K, ?> entry) {
        return entry != null ? entry.getKey() : null;
    }
}
