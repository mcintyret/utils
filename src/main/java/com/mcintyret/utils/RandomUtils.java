package com.mcintyret.utils;

import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

/**
 * User: mcintyret2
 * Date: 22/01/2013
 */
public final class RandomUtils {

    private RandomUtils() {

    }

    private static final Random RNG = new Random();

    public static int nextIntBetween(int min, int max) {
        return RNG.nextInt(max - min) + min;
    }

    public static <T> T randomElement(Collection<T> coll) {
        int index = RNG.nextInt(coll.size());
        if (coll instanceof RandomAccess) {
            return ((List<T>) coll).get(index);
        } else {
            return Iterables.get(coll, index);
        }
    }

}
