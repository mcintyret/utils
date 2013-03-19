package com.mcintyret.utils.collect;

import java.util.*;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
public class CircularArray<E> extends AbstractList<E> {

    private final E[] array;

    private int cursor = 0;

    public CircularArray(E... array) {
        this.array = Arrays.copyOf(array, array.length);
    }

    public CircularArray(Collection<E> c) {
        this(c.size());
        addAll(c);
    }

    public CircularArray(int size) {
        array = (E[]) new Object[size];
    }

    @Override
    public boolean add(E element) {
        array[cursor] = element;
        incrementCursor();
        return true;
    }


    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            int returned = 0;
            int itCursor = cursor;

            @Override
            public boolean hasNext() {
                return returned < size();
            }

            @Override
            public E next() {
                if (hasNext()) {
                    E val = array[itCursor];
                    returned++;
                    incrementCursor();
                    return val;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private void incrementCursor() {
                if (++itCursor == size()) {
                    itCursor = 0;
                }
            }
        };
    }

    @Override
    public int size() {
        return array.length;
    }

    private void incrementCursor() {
        if (++cursor == size()) {
            cursor = 0;
        }
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

    @Override
    public <T> T[] toArray(T[] dest) {
        return (T[]) toArray();
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException("" + index);
        int i = index + cursor;
        if (i >= size()) i -= size();
        return array[i];
    }

    // Unsupported Operations

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int i, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

}
