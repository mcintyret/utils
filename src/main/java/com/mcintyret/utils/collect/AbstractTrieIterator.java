package com.mcintyret.utils.collect;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * User: mcintyret2
 * Date: 11/04/2013
 */
abstract class AbstractTrieIterator<V> extends AbstractIterator<Map.Entry<String, V>> {

    private final Trie<V> trie;

    protected final Stack<Iterator<CharacterAndNode<V>>> iteratorStack = new Stack<>();
    protected Iterator<CharacterAndNode<V>> iterator;


    protected char[] key;
    protected int index = 0;

    protected AbstractTrieIterator(Trie<V> trie, String prefix) {
        this.trie = trie;
        key = Arrays.copyOf(prefix.toCharArray(), 10);
    }


    protected void updateKey(char c) {
        if (c != '\0') {
            if (index == key.length) {
                key = Arrays.copyOf(key, key.length * 2);
            }
            key[index++] = c;
        }
    }


    @Override
    protected void doRemove(Map.Entry<String, V> removed) {
        trie.remove(removed.getKey());
    }

    static <V> Map.Entry<String, V> trieNodeEntry(final TrieNode<V> node, final String key) {
        return new Map.Entry<String, V>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return node.getValue();
            }

            @Override
            public V setValue(V value) {
                return node.setValue(value);
            }
        };
    }

}
