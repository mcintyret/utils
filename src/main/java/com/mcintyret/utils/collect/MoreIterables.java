package com.mcintyret.utils.collect;

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

}
