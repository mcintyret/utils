package com.mcintyret.utils.collect;

import com.google.common.base.Function;

import java.util.Iterator;
import java.util.Map;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public abstract class AbstractMapTrie<V> extends AbstractTrie<V> {

    protected AbstractMapTrie(Function<V, String> keyToValueFunction) {
        super(keyToValueFunction);
    }

    protected AbstractMapTrie(Function<V, String> keyToValueFunction, Sizer sizer) {
        super(keyToValueFunction, sizer);
    }

    protected class AbstractMapTrieNode extends TrieNode {

        private final Map<Character, TrieNode> children;

        public AbstractMapTrieNode(Map<Character, TrieNode> children, V val) {
            super(val);
            this.children = children;
        }

        @Override
        protected TrieNode getChild(char c) {
            return children.get(c);
        }

        @Override
        protected void setChild(char c, TrieNode child) {
            children.put(c, child);
        }

        @Override
        protected boolean isEmpty() {
            return children.isEmpty();
        }

        @Override
        protected void clear() {
            children.clear();
        }

        @Override
        public Iterator<TrieNode> iterator() {
            return children.values().iterator();
        }
    }
}
