package com.mcintyret.utils;

import java.util.Comparator;
import java.util.Map;

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

    public static <C extends Comparable<C>> boolean isBetweenInclusive(C var, C a, C b) {
        return greaterThanOrEqual(a, var) && lessThanOrEqual(var, b);
    }

    public static <C extends Comparable<C>> boolean isBetweenExclusive(C var, C a, C b) {
        return greaterThan(a, var) && lessThan(var, b);
    }

    public static <C extends Comparable<C>> boolean isBetweenInclusiveExclusive(C var, C a, C b) {
        return greaterThanOrEqual(a, var) && lessThan(var, b);
    }

    public static <C extends Comparable<C>> boolean isBetweenExclusiveInclusive(C var, C a, C b) {
        return greaterThan(a, var) && lessThanOrEqual(var, b);
    }

    public static <K> Comparator<K> comparableComparator() {
        return (Comparator<K>) ComparableComparator.INSTANCE;
    }

    public static <C extends Comparable<C>> Comparator<Map.Entry<C, ?>> entryKeyComparator() {
        return new ComparableKeyEntryComparator<>();
    }

    public static <K extends Comparable<K>, V> Comparator<Map.Entry<K, V>> entryComparator(Comparator<K> keyComp) {
        return new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        };
    }

    private static enum ComparableComparator implements Comparator<Object> {
        INSTANCE;
        @Override
        public int compare(Object o1, Object o2) {
            return ((Comparable<Object>) o1).compareTo(o2);
        }
    }

    private static class ComparableKeyEntryComparator<C extends Comparable<C>> implements Comparator<Map.Entry<C, ?>> {
        private static final Comparator<Map.Entry<? extends Comparable, ?>> INSTANCE = new ComparableKeyEntryComparator();

        @Override
        public int compare(Map.Entry<C, ?> o1, Map.Entry<C, ?> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    }
}
