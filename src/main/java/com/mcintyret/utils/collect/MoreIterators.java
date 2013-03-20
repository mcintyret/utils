package com.mcintyret.utils.collect;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
public final class MoreIterators {

    private MoreIterators() {

    }

    public static <T> Iterator<T> skippingIterator(Iterator<T> iterator, int skip) {
        checkArgument(skip > 0);
        return new SkippingIterator<>(iterator, skip);
    }

    public static <T> Iterator<T> forSequence(Sequence<T> sequence, T... init) {
        return new SequenceIterator<>(sequence, init);
    }

    public static <T> Iterator<T> forSequence(Sequence<T> sequence, Collection<T> init) {
        return new SequenceIterator<>(sequence, init);
    }

    public static <T extends Comparable<T>> Iterator<T> forSequence(Sequence<T> sequence, T max, T... init) {
        return new BoundedSequenceIterator<>(sequence, max, Arrays.asList(init));
    }

    public static <T extends Comparable<T>> Iterator<T> forSequence(Sequence<T> sequence, T max, Collection<T> init) {
        return new BoundedSequenceIterator<>(sequence, max, init);
    }

    public static <T> ListIterator<T> forArray(T... array) {
        return new ArrayIterator<>(array);
    }

    public static <T> ListIterator<T> forArray(int start, T... array) {
        return new ArrayIterator<>(start, array);
    }

    public static <T> void clear(Iterator<T> iterator) {
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    public static <T> Iterator skipFirstN(final Iterator<T> iterator, int n) {
        checkArgument(n >= 0);
        if (n == 0) {
            return iterator;
        } else {
            while (n-- > 0 && iterator.hasNext()) {
                iterator.next();
            }
            return new Iterator<T>() {

                boolean nextCalled = false;

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public T next() {
                    nextCalled = true;
                    return iterator.next();
                }

                @Override
                public void remove() {
                    checkState(nextCalled);
                    iterator.remove();
                }
            };
        }
    }
}
