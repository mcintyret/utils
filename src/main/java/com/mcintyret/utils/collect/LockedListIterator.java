package com.mcintyret.utils.collect;

import java.util.ListIterator;
import java.util.concurrent.locks.Lock;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
class LockedListIterator<E> extends LockedIterator<E> implements ListIterator<E> {

    private final ListIterator<E> delegate;

    public LockedListIterator(ListIterator<E> delegate, Lock readLock, Lock writeLock) {
        super(delegate, readLock, writeLock);
        this.delegate = delegate;
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

    @Override
    public boolean hasPrevious() {
        readLock.lock();
        try {
            return delegate.hasPrevious();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public E previous() {
        readLock.lock();
        try {
            return delegate.previous();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int nextIndex() {
        readLock.lock();
        try {
            return delegate.nextIndex();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int previousIndex() {
        readLock.lock();
        try {
            return delegate.previousIndex();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void set(E e) {
        writeLock.lock();
        try {
            delegate.set(e);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void add(E e) {
        writeLock.lock();
        try {
            delegate.add(e);
        } finally {
            writeLock.unlock();
        }
    }
}
