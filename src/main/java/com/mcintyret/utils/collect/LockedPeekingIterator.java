package com.mcintyret.utils.collect;

import com.google.common.collect.PeekingIterator;

import java.util.concurrent.locks.Lock;

/**
 * User: mcintyret2
 * Date: 10/04/2013
 */
public class LockedPeekingIterator<E> extends LockedIterator<E> implements PeekingIterator<E> {

    private final PeekingIterator<E> delegate;

    public LockedPeekingIterator(PeekingIterator<E> delegate, Lock readLock, Lock writeLock) {
        super(delegate, readLock, writeLock);
        this.delegate = delegate;
    }

    @Override
    public E peek() {
        readLock.lock();
        try {
            return delegate.peek();
        } finally {
            readLock.unlock();
        }

    }
}


