package com.mcintyret.utils.collect;

import com.google.common.collect.PeekingIterator;

import java.util.Arrays;

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
    public PeekingIterator<CharacterAndNode<V>> iterator() {
        return iterator(0);
    }

    @Override
    public PeekingIterator<CharacterAndNode<V>> iterator(char c) {
        return iterator(charToIndex(c));
    }

    private PeekingIterator<CharacterAndNode<V>> iterator(final int startingIndex) {
        return new ForwardArrayTrieNodeIterator(startingIndex);
    }

    @Override
    public PeekingIterator<CharacterAndNode<V>> reverseIterator() {
        return reverseIterator(children.length - 1);
    }

    @Override
    public PeekingIterator<CharacterAndNode<V>> reverseIterator(char c) {
        return reverseIterator(charToIndex(c));
    }

    private PeekingIterator<CharacterAndNode<V>> reverseIterator(final int startingIndex) {
       return new ReverseArrayTrieNodeIterator(startingIndex);
    }

    @Override
    public void clear() {
        Arrays.fill(children, null);
    }

    protected abstract int charToIndex(char c);

    protected abstract char indexToChar(int i);

    protected abstract int charsetSize();

    private abstract class ArrayTrieNodeIterator extends AbstractIterator<CharacterAndNode<V>> {

        int index;

        private ArrayTrieNodeIterator(int startingIndex) {
            this.index = startingIndex;
        }

        @Override
        protected void doRemove(CharacterAndNode<V> removed) {
            throw new UnsupportedOperationException();
        }
    }

    private class ForwardArrayTrieNodeIterator extends ArrayTrieNodeIterator {

        private ForwardArrayTrieNodeIterator(int startingIndex) {
            super(startingIndex);
        }

        @Override
        protected CharacterAndNode<V> computeNext() {
            for (;index < children.length; index++) {
                if (children[index] != null) {
                    return new CharacterAndNode<>(indexToChar(index), children[index++]);
                }
            }
            return endOfData();
        }
    }

    private class ReverseArrayTrieNodeIterator extends ArrayTrieNodeIterator {

        private ReverseArrayTrieNodeIterator(int startingIndex) {
            super(startingIndex);
        }

        @Override
        protected CharacterAndNode<V> computeNext() {
            for (;index >= 0; index--) {
                if (children[index] != null) {
                    return new CharacterAndNode<>(indexToChar(index), children[index--]);
                }
            }
            return endOfData();
        }
    }
}
