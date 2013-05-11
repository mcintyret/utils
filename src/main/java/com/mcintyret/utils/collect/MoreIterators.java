package com.mcintyret.utils.collect;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static com.google.common.base.Preconditions.checkArgument;

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

    public static <T> void fill(ListIterator<T> iterator, T t) {
        while (iterator.hasNext()) {
            iterator.next();
            iterator.set(t);
        }
    }

    public static <T> void fillWithNull(ListIterator<T> iterator) {
        fill(iterator, null);
    }

    public static <T> Iterator skipFirstN(final Iterator<T> iterator, int n) {
        Iterators.advance(iterator, n);
        return iterator;
    }

    public static <T> Iterator skipFirstNOrDie(final Iterator<T> iterator, int n) {
        int advanced = Iterators.advance(iterator, n);
        if (advanced != n) {
            throw new IllegalArgumentException("Fewer than " + n + " elements in iterator.");
        }
        return iterator;
    }

    public static <T> Iterator<T> lockedIterator(Iterator<T> iterator, ReadWriteLock lock) {
        return new LockedIterator<>(iterator, lock.readLock(), lock.writeLock());
    }

    public static <T> Iterator<T> lockedIterator(Iterator<T> iterator, Lock lock) {
        return new LockedIterator<>(iterator, lock, lock);
    }

    public static <T> ListIterator<T> lockedListIterator(ListIterator<T> iterator, ReadWriteLock lock) {
        return new LockedListIterator<>(iterator, lock.readLock(), lock.writeLock());
    }

    public static <T> ListIterator<T> lockedListIterator(ListIterator<T> iterator, Lock lock) {
        return new LockedListIterator<>(iterator, lock, lock);
    }

    public static <T> PeekingIterator<T> lockedPeekingIterator(PeekingIterator<T> iterator, ReadWriteLock lock) {
        return new LockedPeekingIterator<>(iterator, lock.readLock(), lock.writeLock());
    }

    public static <T> T nextOrNull(Iterator<T> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }

    public static <F, T> Iterator<T> transform(final Iterator<F> delegate, final Function<? super F, ? extends T> function) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public T next() {
                return function.apply(delegate.next());
            }

            @Override
            public void remove() {
                delegate.remove();
            }
        };
    }

    public static <T> T pollNext(Iterator<T> it) {
        if (it.hasNext()) {
            T ret = it.next();
            it.remove();
            return ret;
        } else {
            return null;
        }
    }

    public static <C extends Comparable<C>> PeekingIterator<C> withinBounds(Iterator<C> it, C from, C to) {
        return withinBounds(it, from, true, to, false);
    }

    public static <C extends Comparable<C>> PeekingIterator<C> withinBounds(Iterator<C> it, C from, boolean fromInclusive, C to, boolean toInclusive) {
        return new BoundIterator<C>(it, from, fromInclusive, to, toInclusive) {
            @Override
            protected int compare(C one, C two) {
                return one.compareTo(two);
            }
        };
    }

    public static <T> PeekingIterator<T> withinBounds(Iterator<T> it, T from, T to, final Comparator<T> comparator) {
        return withinBounds(it, from, true, to, false, comparator);
    }

    public static <T> PeekingIterator<T> withinBounds(Iterator<T> it, T from, boolean fromInclusive, T to, boolean toInclusive, final Comparator<T> comparator) {
        return new BoundIterator<T>(it, from, fromInclusive, to, toInclusive) {
            @Override
            protected int compare(T one, T two) {
                return comparator.compare(one, two);
            }
        };
    }
}
