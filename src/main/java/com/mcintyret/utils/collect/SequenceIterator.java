package com.mcintyret.utils.collect;

import java.util.*;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
class SequenceIterator<E> implements Iterator<E> {

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

    protected E next;

    @Override
    public boolean hasNext() {
        if (next == null) {
            computeNext();
        }
        return next != null;
    }

    @Override
    public E next() {
        if (hasNext()) {
            E next = this.next;
            this.next = null;
            return next;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void computeNext() {
        E next = initialIterator.hasNext() ? initialIterator.next() : sequence.computeNext(prev);
        prev.add(next);
        this.next = next;
    }

}
