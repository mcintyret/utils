package com.mcintyret.utils.collect;

import java.util.Map;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public interface Trie< V> extends Map<String, V> {

    Trie<V> getSubTrie(String key);

    Trie<V> removeSubTrie(String key);

}
