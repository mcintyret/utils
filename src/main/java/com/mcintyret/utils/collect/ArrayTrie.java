package com.mcintyret.utils.collect;


import com.google.common.base.Function;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

import static com.google.common.base.Predicates.isNull;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterators.*;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public abstract class ArrayTrie<V> extends AbstractTrie<V> {

    protected ArrayTrie(Function<V, String> keyFromValueFuction) {
        super(keyFromValueFuction);
    }

    protected ArrayTrie(Function<V, String> keyFromValueFuction, Sizer sizer) {
        super(keyFromValueFuction, sizer);
    }

    @Override
    protected TrieNode newTrieNode(V value) {
        return new ArrayTrieNode(value);
    }

    protected class ArrayTrieNode extends TrieNode {

        private final TrieNode[] children;

        protected ArrayTrieNode(V val) {
            super(val);
            this.children = (TrieNode[]) Array.newInstance(TrieNode.class, charsetSize());
        }

        @Override
        protected TrieNode getChild(char c) {
            return children[charToIndex(c)];
        }

        @Override
        public void setChild(char c, TrieNode child) {
            children[charToIndex(c)] = child;
        }

        @Override
        public boolean isEmpty() {
            return all(forArray(children), isNull());
        }

        @Override
        public Iterator<TrieNode> iterator() {
            return filter(forArray(children), notNull());
        }

        @Override
        public void clear() {
            Arrays.fill(children, null);
        }
    }

    protected abstract int charToIndex(char c);

    protected abstract int charsetSize();
}
