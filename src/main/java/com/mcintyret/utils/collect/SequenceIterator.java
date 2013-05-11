package com.mcintyret.utils.collect;

import java.util.*;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
class SequenceIterator<E> extends AbstractIterator<E> {

    private final Sequence<E> sequence;

    private final List<E> prev;

    private Iterator<E> initialIterator;

    public SequenceIterator(Sequence<E> sequence, E... prev) {
        this(sequence, Arrays.asList(prev));
    }

    public SequenceIterator(Sequence<E> sequence, Collection<E> prev) {
        this.sequence = sequence;
        this.prev = new CircularArray<>(prev);
        this.initialIterator = prev.iterator();
    }

    @Override
    protected void doRemove(E removed) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected E computeNext() {
        E next = initialIterator.hasNext() ? initialIterator.next() : sequence.computeNext(prev);
        if (next == null) {
            return endOfData();
        } else {
            prev.add(next);
            return next;
        }
    }
}
