package com.mcintyret.utils;

import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.Random;

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

    public static <T> T randomElement(T[] array) {
        return array[RNG.nextInt(array.length)];
    }

    public static byte randomElement(byte[] array) {
        return array[RNG.nextInt(array.length)];
    }

    public static char randomElement(char[] array) {
        return array[RNG.nextInt(array.length)];
    }

    public static int randomElement(int[] array) {
        return array[RNG.nextInt(array.length)];
    }

    public static short randomElement(short[] array) {
        return array[RNG.nextInt(array.length)];
    }

    public static boolean randomElement(boolean[] array) {
        return array[RNG.nextInt(array.length)];
    }

    public static long randomElement(long[] array) {
        return array[RNG.nextInt(array.length)];
    }

    public static float randomElement(float[] array) {
        return array[RNG.nextInt(array.length)];
    }

    public static double randomElement(double[] array) {
        return array[RNG.nextInt(array.length)];
    }

    public static <T> T randomElement(Collection<T> coll) {
        int index = RNG.nextInt(coll.size());
        return Iterables.get(coll, index);
    }

}
