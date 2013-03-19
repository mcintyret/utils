package com.mcintyret.utils.heap;

import java.util.Collection;
import java.util.Comparator;

/**
 * User: mcintyret2
 * Date: 26/02/2013
 */
public class BinaryMinHeap<T> extends BinaryHeap<T> implements MinHeap<T> {

    protected BinaryMinHeap() {
        super(true);
    }

    protected BinaryMinHeap(int capacity) {
        super(true, capacity);
    }

    protected BinaryMinHeap(Collection<? extends T> c) {
        super(true, c);
    }

    protected BinaryMinHeap(int capacity, Collection<? extends T> c) {
        super(true, capacity, c);
    }

    protected BinaryMinHeap(int capacity, Comparator<T> tComparator, Collection<? extends T> c) {
        super(true, capacity, tComparator, c);
    }

    @Override
    public T peekMin() {
        return peekTop();
    }

    @Override
    public T removeMin() {
        return removeTop();
    }

    @Override
    public void merge(Heap<T> ts) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
