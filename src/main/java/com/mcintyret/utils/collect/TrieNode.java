package com.mcintyret.utils.collect;

import com.google.common.collect.PeekingIterator;

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

    PeekingIterator<CharacterAndNode<V>> iterator();

    PeekingIterator<CharacterAndNode<V>> iterator(char c);

    PeekingIterator<CharacterAndNode<V>> reverseIterator();

    PeekingIterator<CharacterAndNode<V>> reverseIterator(char c);
}
