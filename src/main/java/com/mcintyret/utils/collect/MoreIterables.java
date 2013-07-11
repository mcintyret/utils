package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
public final class MoreIterables {

    private MoreIterables() {

    }

    public static <T> Iterable<T> of(final Iterator<T> iterator) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }

    public static <T> void clear(Iterable<T> iterable) {
        MoreIterators.clear(iterable.iterator());
    }

    public static <T> Collection<T> toCollection(final Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            return (Collection<T>) iterable;
        } else {
            return new AbstractCollection<T>() {
                @Override
                public Iterator<T> iterator() {
                    return iterable.iterator();
                }

                @Override
                public int size() {
                    return Iterators.size(iterator());
                }

                @Override
                public boolean isEmpty() {
                    return !iterator().hasNext();
                }
            };
        }
    }
}
