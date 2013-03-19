package com.mcintyret.utils;

/**
 * User: mcintyret2
 * Date: 26/02/2013
 */
public class Utils {

    public static <T> void swap(T[] a, int i, int j) {
        if (i != j) {
            T tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }


}
