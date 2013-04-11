package com.mcintyret.utils.collect;

import com.google.common.collect.ForwardingNavigableMap;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public abstract class ForwardingTrie<T> extends ForwardingNavigableMap<String, T> implements Trie<T> {

    @Override
    public Trie<T> getSubTrie(String key) {
        return delegate().getSubTrie(key);
    }

    @Override
    public Trie<T> removeSubTrie(String key) {
        return delegate().removeSubTrie(key);
    }

    @Override
    public Trie<T> subMap(String fromKey, boolean fromInclusive, String toKey, boolean toInclusive) {
        return delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public Trie<T> headMap(String toKey, boolean inclusive) {
        return delegate().headMap(toKey, inclusive);
    }

    @Override
    public Trie<T> tailMap(String fromKey, boolean inclusive) {
        return delegate().tailMap(fromKey, inclusive);
    }

    @Override
    public Trie<T> descendingMap() {
        return delegate().descendingMap();
    }

    @Override
    protected abstract Trie<T> delegate();
}
