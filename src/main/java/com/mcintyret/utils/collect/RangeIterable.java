package com.mcintyret.utils.collect;

import com.google.common.base.Predicates;
import com.google.common.collect.Range;

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

    private final Range<T> range;

    private final Sequence<T> sequence;

    private final Collection<T> init;

    public RangeIterable(Collection<T> from, T to, Sequence<T> sequence) {
        checkArgument(all(from, Predicates.<Object>notNull()));
        checkArgument(from.size() >= 1);
        checkNotNull(to);
        this.sequence = sequence;
        this.init = from;
        this.range = Range.closed(from.iterator().next(), to);
    }

    public RangeIterable(T from, T to, Sequence<T> sequence) {
        this(singleton(from), to, sequence);
    }


    @Override
    public Iterator<T> iterator() {
        return MoreIterators.forSequence(sequence, range.upperEndpoint(), init);
    }

    public Range<T> asRange() {
        return range;
    }

    @Override
    public String toString() {
        return range.toString();
    }

    @Override
    public int hashCode() {
        return range.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || ((o instanceof RangeIterable) &&
                ((RangeIterable) o).range.equals(range));
    }

}
