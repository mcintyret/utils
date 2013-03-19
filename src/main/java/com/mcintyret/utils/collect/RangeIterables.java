package com.mcintyret.utils.collect;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: mcintyret2
 * Date: 17/03/2013
 */
public final class RangeIterables {

    private RangeIterables() {
        // non-instantiable
    }

    public static RangeIterable<String> ofRange(String from, String to) {
        checkNotNull(from);
        checkNotNull(to);
        checkArgument(from.length() == to.length());
        return new RangeIterable<>(from, to, Sequences.lexicographicSequence());
    }

    public static RangeIterable<Integer> ofRange(int from, int to) {
        return ofRangeWithSkip(from, to, 1);
    }

    public static RangeIterable<Long> ofRange(long from, long to) {
        return ofRangeWithSkip(from, to, 1L);
    }

    public static RangeIterable<Integer> ofRangeWithSkip(int from, int to, int skip) {
        return new RangeIterable<>(from, to, Sequences.summingSequence(skip));
    }

    public static RangeIterable<Long> ofRangeWithSkip(long from, long to, long skip) {
        return new RangeIterable<>(from, to, Sequences.summingSequence(skip));
    }

    public static RangeIterable<Long> powersOf(int num, long max) {
        return new RangeIterable<>(1L, max, Sequences.multiplyingSequence(num));
    }

    public static RangeIterable<Long> powersOf(int num) {
        return powersOf(num, Long.MAX_VALUE);
    }

    public static RangeIterable<Long> multiplesOf(long num, long max) {
        return new RangeIterable<>(0L, max, Sequences.summingSequence(num));
    }

    public static RangeIterable<Long> multiplesOf(long num) {
        return multiplesOf(num, Long.MAX_VALUE);
    }

    public static RangeIterable<Long> fibonacci() {
        return fibonacci(Long.MAX_VALUE);
    }

    public static RangeIterable<Long> fibonacci(long max) {
        return new RangeIterable<>(Arrays.asList(0l, 1l), max, Sequences.fibonacci());
    }


}
