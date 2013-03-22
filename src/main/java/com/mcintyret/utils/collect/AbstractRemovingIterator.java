package com.mcintyret.utils.collect;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkState;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public abstract class AbstractRemovingIterator<T> implements Iterator<T> {

    private T last;

    @Override
    public final T next() {
        last = doNext();
        return last;
    }

    protected abstract T doNext();

    @Override
    public final void remove() {
        checkState(last != null);
        doRemove(last);
        last = null;
    }

    protected abstract void doRemove(T removed);
}
