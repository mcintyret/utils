package com.mcintyret.utils.collect;

import javax.annotation.Nullable;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.*;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public class ArrayIterator<E> implements ListIterator<E> {

    private final E[] array;

    int index;
    int lastIndex = -1;

    public ArrayIterator(int start, E... array) {
        checkNotNull(array);
        checkPositionIndex(start, array.length);
        this.array = array;
        this.index = start;
    }

    public ArrayIterator(E... array) {
        this(0, array);
    }

    @Override
    public boolean hasNext() {
        return isInRange(index);
    }

    @Override
    public E next() {
        if (hasNext()) {
            lastIndex = index++;
            return array[lastIndex];
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean hasPrevious() {
        return isInRange(index - 1);
    }

    @Override
    public E previous() {
        if (hasPrevious()) {
            lastIndex = --index;
            return array[lastIndex];
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int nextIndex() {
        return index;
    }

    @Override
    public int previousIndex() {
        return index - 1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(@Nullable E e) {
        checkState(lastIndex >= 0);
        array[lastIndex] = e;
        lastIndex = -1;
    }

    @Override
    public void add(E e) {
        throw new UnsupportedOperationException();
    }

    private boolean isInRange(int index) {
        return index >= 0 && index < array.length;
    }

}
