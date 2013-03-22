package com.mcintyret.utils.collect;


import com.google.common.base.Function;

import java.util.HashMap;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public class HashTrie<V> extends AbstractMapTrie<V> {

    public HashTrie(Function<V, String> keyToValueFunction) {
        super(keyToValueFunction);
    }

    @Override
    protected TrieNode newTrieNode(V value) {
        return new HashTrieNode(value);
    }

    private class HashTrieNode extends AbstractMapTrieNode {

        protected HashTrieNode(V val) {
            super(new HashMap<Character, TrieNode>(), val);
        }
    }
}
