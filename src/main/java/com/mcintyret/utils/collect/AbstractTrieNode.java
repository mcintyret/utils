package com.mcintyret.utils.collect;

/**
* User: mcintyret2
* Date: 04/04/2013
*/
abstract class AbstractTrieNode<V> implements TrieNode<V> {

    private V val;

    AbstractTrieNode(V val) {
        this.val = val;
    }

    @Override
    public V getValue() {
        return val;
    }

    @Override
    public V setValue(V value) {
        V oldVal = val;
        this.val = value;
        return oldVal;
    }

}
