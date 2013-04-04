package com.mcintyret.utils;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.Map;

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

    public static <K, V> Function<Map.Entry<K, V>, K> keyFunction() {
        return new Function<Map.Entry<K, V>, K>() {
            @Override
            public K apply(Map.Entry<K, V> input) {
                return input.getKey();
            }
        };
    }

    public static <K, V> Function<Map.Entry<K, V>, V> valueFunction() {
        return new Function<Map.Entry<K, V>, V>() {
            @Override
            public V apply(Map.Entry<K, V> input) {
                return input.getValue();
            }
        };
    }

    public static <K, V> Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> iterator) {
        return Iterators.transform(iterator, Utils.<K, V>keyFunction());
    }

    public static <K, V> Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> iterator) {
        return Iterators.transform(iterator, Utils.<K, V>valueFunction());
    }

    public static int[] upTo(int upTo) {
        int[] a = new int[upTo];
        for (int i = 0; i < upTo; i++) {
            a[i] = i;
        }
        return a;
    }

}
