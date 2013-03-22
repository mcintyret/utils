package com.mcintyret.utils.collect;

import com.google.common.collect.ForwardingMap;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public abstract class ForwardingTrie<T> extends ForwardingMap<String, T> implements Trie<T> {

    @Override
    public void add(T val) {
        delegate().add(val);
    }

    @Override
    public Trie<T> subTrie(String key) {
        return delegate().subTrie(key);
    }

    @Override
    protected abstract Trie<T> delegate();
}
