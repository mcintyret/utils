package com.mcintyret.utils.collect;

import java.util.Arrays;
import java.util.Iterator;

/**
* User: mcintyret2
* Date: 04/04/2013
*/
abstract class AbstractArrayTrieNode<V> extends AbstractTrieNode<V> {

    private final TrieNode<V>[] children;

    AbstractArrayTrieNode(V val) {
        super(val);
        this.children = (TrieNode<V>[]) new TrieNode[charsetSize()];
    }

    @Override
    public TrieNode<V> getChild(char c) {
        return children[charToIndex(c)];
    }

    @Override
    public void setChild(char c, TrieNode<V> child) {
        children[charToIndex(c)] = child;
    }

    @Override
    public boolean isEmpty() {
        for (TrieNode<V> child : children) {
            if (child != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<CharacterAndNode<V>> iterator() {
        return new AbstractHasNextFetchingIterator<CharacterAndNode<V>>() {
            int index = 0;

            @Override
            protected boolean doHasNext() {
                for (; index < children.length; index++) {
                    if (children[index] != null) {
                        setNext(new CharacterAndNode<>(indexToChar(index), children[index]));
                        index++;
                        return true;
                    }
                }
                return false;
            }

            @Override
            protected void doRemove(CharacterAndNode<V> removed) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public void clear() {
        Arrays.fill(children, null);
    }

    protected abstract int charToIndex(char c);

    protected abstract char indexToChar(int i);

    protected abstract int charsetSize();
}
