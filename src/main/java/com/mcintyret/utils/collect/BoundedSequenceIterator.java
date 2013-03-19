package com.mcintyret.utils.collect;

import java.util.Collection;

import static com.mcintyret.utils.Comparables.lessThanOrEqual;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
class BoundedSequenceIterator<E extends Comparable<E>> extends SequenceIterator<E> {

    private final E max;

    public BoundedSequenceIterator(Sequence<E> sequence, E max, Collection<E> prev) {
        super(sequence, prev);
        this.max = max;
    }

    @Override
    public boolean hasNext() {
        return super.hasNext() && lessThanOrEqual(next, max);
    }
}
