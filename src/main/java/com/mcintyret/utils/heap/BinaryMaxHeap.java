package com.mcintyret.utils.heap;

import java.util.Collection;
import java.util.Comparator;

/**
 * User: mcintyret2
 * Date: 26/02/2013
 */
public class BinaryMaxHeap<T> extends BinaryHeap<T> implements MaxHeap<T> {

    protected BinaryMaxHeap() {
        super(true);
    }

    protected BinaryMaxHeap(int capacity) {
        super(true, capacity);
    }

    protected BinaryMaxHeap(Collection<? extends T> c) {
        super(true, c);
    }

    protected BinaryMaxHeap(int capacity, Collection<? extends T> c) {
        super(true, capacity, c);
    }

    protected BinaryMaxHeap(int capacity, Comparator<T> tComparator, Collection<? extends T> c) {
        super(true, capacity, tComparator, c);
    }

    @Override
    public T peekMax() {
        return peekTop();
    }

    @Override
    public T removeMax() {
        return removeTop();
    }

    @Override
    public void merge(Heap<T> ts) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
