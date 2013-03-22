package com.mcintyret.utils.collect;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
class LockedIterator<E> implements Iterator<E> {

    protected final Lock readLock;

    protected final Lock writeLock;

    private final Iterator<E> delegate;

    LockedIterator(Iterator<E> delegate, Lock readLock, Lock writeLock) {
        this.delegate = delegate;
        this.readLock = readLock;
        this.writeLock = writeLock;
    }

    @Override
    public boolean hasNext() {
        readLock.lock();
        try {
            return delegate.hasNext();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public E next() {
        readLock.lock();
        try {
            return delegate.next();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void remove() {
        writeLock.lock();
        try {
            delegate.remove();
        } finally {
            writeLock.unlock();
        }
    }
}
