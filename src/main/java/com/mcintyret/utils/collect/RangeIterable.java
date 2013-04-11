package com.mcintyret.utils.collect;

import com.google.common.base.Predicates;

import java.util.Collection;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.all;
import static java.util.Collections.singleton;

/**
 * User: mcintyret2
 * Date: 17/03/2013
 */
public class RangeIterable<T extends Comparable<T>> implements Iterable<T> {

    private final Sequence<T> sequence;

    private final Collection<T> init;

    private final T upperEndpoint;

    public RangeIterable(Collection<T> from, T to, Sequence<T> sequence) {
        checkArgument(from.size() >= 1);
        checkArgument(all(from, Predicates.<Object>notNull()));
        checkNotNull(to);
        this.sequence = sequence;
        this.init = from;
        this.upperEndpoint = to;
    }

    public RangeIterable(T from, T to, Sequence<T> sequence) {
        this(singleton(from), to, sequence);
    }

    @Override
    public Iterator<T> iterator() {
        return MoreIterators.forSequence(sequence, upperEndpoint, init);
    }


}
