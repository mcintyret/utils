package com.mcintyret.utils.collect;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkState;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public abstract class AbstractRemovingIterator<T> implements Iterator<T> {

    private final Iterator<T> delegate;

    public AbstractRemovingIterator(Iterator<T> delegate) {
        this.delegate = delegate;
    }

    private T last;

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public T next() {
        last = delegate.next();
        return last;
    }

    @Override
    public void remove() {
        checkState(last != null);
        try {
            delegate.remove();
        } catch (UnsupportedOperationException ignored) {
            // do nothing
        }
        doRemove(last);
        last = null;
    }

    protected abstract void doRemove(T removed);
}
