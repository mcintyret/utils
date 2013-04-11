package com.mcintyret.utils.collect;

import java.util.NavigableMap;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public interface Trie< V> extends NavigableMap<String, V> {

    // New Trie methods
    Trie<V> getSubTrie(String key);

    Trie<V> removeSubTrie(String key);

    // Overriding NavigableMap methods to return Trie
    Trie<V> subMap(String fromKey, boolean fromInclusive, String toKey, boolean toInclusive);

    Trie<V> headMap(String toKey, boolean toInclusive);

    Trie<V> tailMap(String fromKey, boolean fromInclusive);

    Trie<V> descendingMap();


}
