package com.mcintyret.utils;

import java.util.Comparator;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
public final class Comparables {

    private Comparables() {

    }

    public static <C extends Comparable<C>> boolean greaterThan(C a, C b) {
        return a.compareTo(b) > 0;
    }

    public static <C extends Comparable<C>> boolean greaterThanOrEqual(C a, C b) {
        return a.compareTo(b) >= 0;
    }

    public static <C extends Comparable<C>> boolean lessThan(C a, C b) {
        return a.compareTo(b) < 0;
    }

    public static <C extends Comparable<C>> boolean lessThanOrEqual(C a, C b) {
        return a.compareTo(b) <= 0;
    }

    public static <C extends Comparable<C>> boolean equal(C a, C b) {
        return a.compareTo(b) == 0;
    }

    public static <K> Comparator<K> comparableComparator() {
        return (Comparator<K>) ComparableComparator.INSTANCE;
    }

    private static enum ComparableComparator implements Comparator<Object> {
        INSTANCE;
        @Override
        public int compare(Object o1, Object o2) {
            return ((Comparable<Object>) o1).compareTo(o2);
        }
    };
}
