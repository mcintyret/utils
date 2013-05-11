package com.mcintyret.utils.collect;

import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 08/04/2013
 */
abstract class BoundIterator<T> extends AbstractIterator<T>  {

    private final Iterator<T> delegate;

    private final T from;
    private final boolean fromInclusive;
    private final T to;
    private final boolean toInclusive;

    public BoundIterator(Iterator<T> delegate, T from, boolean fromInclusive, T to, boolean toInclusive) {
        this.from = from;
        this.fromInclusive = fromInclusive;
        this.to = to;
        this.toInclusive = toInclusive;
        this.delegate = delegate;
    }

    @Override
    protected T computeNext() {
        if (delegate.hasNext()) {
            T next = delegate.next();
            if (validBottom(next) && validTop(next)) {
                return next;
            }
        }
        return endOfData();
    }

    private boolean validBottom(T val) {
        return from == null || (fromInclusive ? compare(from, val) <= 0 : compare(from, val) < 0);
    }

    private boolean validTop(T val) {
        return to == null || (toInclusive ? compare(val, to) <= 0 : compare(val, to) < 0);
    }

    protected abstract int compare(T one, T two);

    @Override
    protected void doRemove(T removed) {
        delegate.remove();
    }
}
