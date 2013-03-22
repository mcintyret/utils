package com.mcintyret.utils.collect;

import java.util.Map;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public interface Trie< V> extends Map<String, V> {

    void add(V val);

    Trie<V> subTrie(String key);

}
