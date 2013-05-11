package com.mcintyret.utils;

/**
 * User: mcintyret2
 * Date: 26/04/2013
 */
public final class MoreObjects {

    public static <T> T firstNonNull(T first, T second) {
        return first == null ? second : first;
    }

    public static <T> T firstNonNull(T first, T second, T third) {
        return first != null ? first : firstNonNull(second, third);
    }

    public static <T> T firstNonNull(T first, T second, T third, T fourth) {
        return firstNonNull(firstNonNull(first, second), firstNonNull(third, fourth));
    }

    public static <T> T firstNonNull(T first, T second, T third, T fourth, T... rest) {
        T res = firstNonNull(first, second, third, fourth);
        if (res == null) {
            for (T t : rest) {
                if (t != null) {
                    return t;
                }
            }
            return null;
        } else {
            return res;
        }
    }
}
