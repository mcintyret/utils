package com.mcintyret.utils.collect;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * User: mcintyret2
 * Date: 10/04/2013
 */
public class SortedMapSetView<K> extends MapSetView<K> implements SortedSet<K> {

    private final SortedMap<K, ?> map;

    public SortedMapSetView(SortedMap<K, ?> map) {
        super(map);
        this.map = map;
    }

    @Override
    public Comparator<? super K> comparator() {
        return map.comparator();
    }

    @Override
    public SortedSet<K> subSet(K fromElement, K toElement) {
        return new SortedMapSetView<>(map.subMap(fromElement, toElement));
    }

    @Override
    public SortedSet<K> headSet(K toElement) {
        return new SortedMapSetView<>(map.headMap(toElement));
    }

    @Override
    public SortedSet<K> tailSet(K fromElement) {
        return new SortedMapSetView<>(map.tailMap(fromElement));
    }

    @Override
    public K first() {
        return map.firstKey();
    }

    @Override
    public K last() {
        return map.lastKey();
    }

}