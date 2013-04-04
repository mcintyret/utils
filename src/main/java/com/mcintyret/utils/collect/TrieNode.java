package com.mcintyret.utils.collect;

import java.io.Serializable;

/**
 * User: mcintyret2
 * Date: 04/04/2013
 */
interface TrieNode<V> extends Serializable, Iterable<CharacterAndNode<V>> {

    V getValue();

    V setValue(V value);

    TrieNode<V> getChild(char c);

    void setChild(char c, TrieNode<V> child);

    boolean isEmpty();

    void clear();
}
