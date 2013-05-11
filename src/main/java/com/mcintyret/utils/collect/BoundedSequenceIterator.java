package com.mcintyret.utils.collect;

import java.util.Collection;

import static com.mcintyret.utils.Comparables.equal;
import static com.mcintyret.utils.Comparables.lessThan;

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

    private boolean finished = false;

    @Override
    protected E computeNext() {
        if (finished) {
            return endOfData();
        } else {
            E next = super.computeNext();
            if (next == null) {
                return endOfData();
            } else if (lessThan(next, max)) {
                return next;
            } else if (equal(next, max)) {
                finished = true;
                return next;
            } else {
                return endOfData();
            }
        }
    }
}
