package com.mcintyret.utils;

import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * User: mcintyret2
 * Date: 05/04/2013
 */
public class AggregatingComparator<E> implements Comparator<E> {

    public static <T> Comparator<T> forComparators(List<? extends Comparator<T>> comparators) {
        checkArgument(comparators.size() > 0);
        return comparators.size() == 1 ? comparators.get(0) : new AggregatingComparator<T>(comparators);
    }

    private final List<? extends Comparator<E>> comparators;

    private AggregatingComparator(List<? extends Comparator<E>> comparators) {
        this.comparators = comparators;
    }

    @Override
    public int compare(E o1, E o2) {
        for (Comparator<E> comparator : comparators) {
            int comp = comparator.compare(o1, o2);
            if (comp != 0) {
                return comp;
            }
        }
        return 0;
    }

}
