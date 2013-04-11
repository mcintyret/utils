package com.mcintyret.utils.collect;

import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 10/04/2013
 */
public class AbstractDelegatingIterator<T> extends AbstractIterator<T> {

    protected final Iterator<T> delegate;

    public AbstractDelegatingIterator(Iterator<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected T computeNext() {
        return delegate.hasNext() ? delegate.next() : endOfData();
    }

    @Override
    protected void doRemove(T removed) {
        delegate.remove();
    }
}
