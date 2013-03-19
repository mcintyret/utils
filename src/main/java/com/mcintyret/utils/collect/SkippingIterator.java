package com.mcintyret.utils.collect;

import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
class SkippingIterator<E> implements Iterator<E> {

    private final Iterator<E> delegate;

    private final int skip;

    private boolean skipped = false;

    public SkippingIterator(Iterator<E> delegate, int skip) {
        this.delegate = delegate;
        this.skip = skip;
    }

    @Override
    public boolean hasNext() {
        if (!skipped) {
            for (int i = 0; i < skip; i++) {
                if (delegate.hasNext()) {
                    delegate.next();
                }
            }
            skipped = true;
        }
        return delegate.hasNext();
    }

    @Override
    public E next() {
        hasNext();
        E next = delegate.next();
        skipped = false;
        return next;
    }

    @Override
    public void remove() {
        delegate.remove();
    }
}
