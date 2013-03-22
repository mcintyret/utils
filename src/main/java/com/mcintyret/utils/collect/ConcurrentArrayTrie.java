package com.mcintyret.utils.collect;


import com.google.common.base.Function;

import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.mcintyret.utils.collect.MoreIterators.lockedIterator;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public abstract class ConcurrentArrayTrie<V> extends ArrayTrie<V> {

    protected ConcurrentArrayTrie(Function<V, String> keyToValueFunction) {
        super(keyToValueFunction, new AtomicSizer());
    }

    @Override
    protected TrieNode newTrieNode(V value) {
        return new ConcurrentArrayTrieNode(value);
    }

    private class ConcurrentArrayTrieNode extends ArrayTrieNode {

        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private ConcurrentArrayTrieNode(V val) {
            super(val);
        }

        @Override
        protected TrieNode getChild(char c) {
            lock.readLock().lock();
            try {
                return super.getChild(c);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public void setChild(char c, TrieNode child) {
            lock.writeLock().lock();
            try {
                super.setChild(c, child);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public boolean isEmpty() {
            lock.readLock().lock();
            try {
                return super.isEmpty();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public Iterator<TrieNode> iterator() {
            return lockedIterator(super.iterator(), lock);
        }

        @Override
        public void clear() {
            lock.writeLock().lock();
            try {
                super.clear();
            } finally {
                lock.writeLock().unlock();
            }
        }
    }
}
