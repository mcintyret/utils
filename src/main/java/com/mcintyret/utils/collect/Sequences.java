package com.mcintyret.utils.collect;

import java.util.Iterator;
import java.util.List;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
public final class Sequences {

    private Sequences() {

    }

    public static Sequence<String> lexicographicSequence() {
        return LexicographicSequence.INSTANCE;
    }

    private static enum LexicographicSequence implements Sequence<String> {
        INSTANCE;

        @Override
        public String computeNext(List<String> prev) {
            String previous = prev.get(0);
            int pos = previous.length() - 1;
            char[] chars = previous.toCharArray();
            while (pos >= 0 && chars[pos] == 'z') {
                chars[pos--] = 'a';
            }
            if (pos < 0) {
                return new StringBuilder().append(chars).append('a').toString();
            } else {
                chars[pos]++;
                return new String(chars);
            }
        }
    }

    public static void main(String[] args) {
        Iterator<String> it = new SequenceIterator<String>(LexicographicSequence.INSTANCE, "aaa");
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public static Sequence<Integer> summingSequence(final int val) {
        return new IncreasingNumericSequence<Integer>() {
            @Override
            public Integer doComputeNext(List<Integer> previous) {
                return previous.get(0) + val;
            }
        };
    }

    public static Sequence<Long> summingSequence(final long val) {
        return new IncreasingNumericSequence<Long>() {
            @Override
            public Long doComputeNext(List<Long> previous) {
                return previous.get(0) + val;
            }
        };
    }

    public static Sequence<Long> multiplyingSequence(final int val) {
        return new IncreasingNumericSequence<Long>() {
            @Override
            public Long doComputeNext(List<Long> previous) {
                return previous.get(0) * val;
            }
        };
    }

    public static Sequence<Long> fibonacci() {
        return new IncreasingNumericSequence<Long>() {
            @Override
            Long doComputeNext(List<Long> previous) {
                return previous.get(0) + previous.get(1);
            }
        };
    }

    private abstract static class IncreasingNumericSequence<N extends Number> implements Sequence<N> {

        @Override
        public final N computeNext(List<N> previous) {
            N backOne = previous.get(previous.size() - 1);
            N last = doComputeNext(previous);
            if (last.longValue() < backOne.longValue()) {
                // overflow!
                return null;
            } else {
                return last;
            }
        }

        abstract N doComputeNext(List<N> previous);
    }
}
