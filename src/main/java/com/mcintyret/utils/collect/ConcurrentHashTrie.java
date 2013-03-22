package com.mcintyret.utils.collect;


import com.google.common.base.Function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public class ConcurrentHashTrie<V> extends AbstractMapTrie<V> {

    protected ConcurrentHashTrie(Function<V, String> keyFromValueFunction) {
        super(keyFromValueFunction, new AtomicSizer());
    }

    protected ConcurrentHashTrie(Function<V, String> keyFromValueFunction, Map<String, V> map) {
        this(keyFromValueFunction);
        putAll(map);
    }


    @Override
    protected TrieNode newTrieNode(V value) {
        return new ConcurrentHashTrieNode(value);
    }

    protected class ConcurrentHashTrieNode extends AbstractMapTrieNode {

        public ConcurrentHashTrieNode(V val) {
            super(new ConcurrentHashMap<Character, TrieNode>(), val);
        }
    }
}
