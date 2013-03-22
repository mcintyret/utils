package com.mcintyret.utils.collect;

import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public abstract class AbstractDelegatingRemovingIterator<T> extends AbstractRemovingIterator<T> {

    private final Iterator<T> delegate;

    public AbstractDelegatingRemovingIterator(Iterator<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    protected T doNext() {
        return delegate.next();
    }

    @Override
    protected void doRemove(T removed) {
        // UnsupportedOperationExceptions are propagated, and prevent any additional stuff happening
        delegate.remove();
        doRemove2(removed);
    }

    // TODO: better name!
    protected abstract void doRemove2(T removed);
}
