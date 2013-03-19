package com.mcintyret.utils.heap;

import com.mcintyret.utils.Comparables;
import com.mcintyret.utils.Utils;

import java.util.*;

/**
 * User: mcintyret2
 * Date: 12/02/2013
 */
abstract class BinaryHeap<T> extends AbstractCollection<T> implements Heap<T> {

    private static final int DEFAULT_CAPACITY = 1024;

    private final Comparator<T> comparator;

    private final T[] heap;

    private int i = 0;

    private int modCount = 0;

    private final boolean max;

    protected BinaryHeap(boolean max) {
        this(max, DEFAULT_CAPACITY);
    }

    protected BinaryHeap(boolean max, int capacity) {
        this(max, capacity, null);
    }

    protected BinaryHeap(boolean max, Collection<? extends T> c) {
        this(max, DEFAULT_CAPACITY, c);
    }


    protected BinaryHeap(boolean max, int capacity, Collection<? extends T> c) {
        this(max, capacity, Comparables.<T>comparableComparator(), c);
    }

    protected BinaryHeap(boolean max, int capacity, Comparator<T> comparator, Collection<? extends T> c) {
        this.comparator = comparator;
        heap = (T[]) new Object[capacity];
        this.max = max;
        if (c != null) {
            addAll(c);
        }
    }

    @Override
    public boolean add(T val) {
        if (isFull()) {
            throw new IllegalArgumentException("Heap Full!");
        }
        int pos = i;
        heap[i++] = val;

        while (pos != 0) {
            int parent = (pos-1)/2;
            if (appearsBefore(pos, parent)) {
                Utils.swap(heap, pos, parent);
                pos = parent;
            } else {
                break;
            }
        }
        modCount++;
        return true;
    }


    protected T peekTop() {
        return heap[0];
    }

    protected T removeTop() {
        T min = heap[0]; // store the min;
        heap[0] = heap[--i];

        int pos = 0;
        while (true) {
            int l = 2 * pos + 1;
            int r = 2 * pos + 2;

            boolean lSmaller = l < i && appearsBefore(l, pos);
            boolean rSmaller = r < i && appearsBefore(r, pos);

            if (lSmaller && rSmaller) {
                if (appearsBefore(l, r)) {
                    Utils.swap(heap, pos, l);
                    pos = l;
                } else {
                    Utils.swap(heap, pos, r);
                    pos = r;
                }
            } else if (lSmaller) {
                Utils.swap(heap, pos, l);
                pos = l;
            } else if (rSmaller) {
                Utils.swap(heap, pos, r);
                pos = r;
            } else {
                break;
            }
        }
        modCount++;
        return min;
    }

    private boolean appearsBefore(int lhs, int rhs) {
        return max ^ comparator.compare(heap[lhs], heap[rhs]) < 0;
    }

    @Override
    public Iterator<T> iterator() {
        final int expectedModCount = modCount;
        return new Iterator<T>() {
            int index = 0;

            void checkForComodification() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                return index < i;
            }

            @Override
            public T next() {
                checkForComodification();
                if (hasNext()) {
                    return heap[index++];
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
               throw new UnsupportedOperationException();
            }
        };
    }

    public int size() {
        return i;
    }

    public boolean isFull() {
        return size() == heap.length;
    }
}