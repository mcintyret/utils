package com.mcintyret.utils.collect;

import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 08/04/2013
 */
public class UpperBoundIterator<C extends Comparable<C>> extends AbstractIterator<C>  {

    private final Iterator<C> delegate;

    private final C max;

    public UpperBoundIterator(Iterator<C> delegate, C max) {
        this.delegate = delegate;
        this.max = max;
    }

    @Override
    protected C computeNext() {
        if (delegate.hasNext()) {
            C next = delegate.next();
            if (next.compareTo(max) < 0) {
                return next;
            }
        }
        return endOfData();
    }

    @Override
    protected void doRemove(C removed) {
        delegate.remove();
    }
}
