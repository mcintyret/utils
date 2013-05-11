package com.mcintyret.utils;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * User: mcintyret2
 * Date: 19/03/2013
 */
public final class EnumUtils {

    private EnumUtils() {

    }

    public static <E extends Enum<E>> Set<E> enumSetFromOrdinals(Class<E> enumClass, int... ordinals) {
        Set<E> set;
        if (ordinals == null || ordinals.length == 0) {
            set = EnumSet.allOf(enumClass);
        } else {
            set = EnumSet.noneOf(enumClass);
            E[] vals = enumClass.getEnumConstants();
            for (int i : ordinals) {
                set.add(vals[i]);
            }
        }
        return set;
    }

    public static <V, E extends Enum<E>> Map<V, E> toMap(Class<E> enumClass, Function<E, V> function) {
        ImmutableMap.Builder<V, E> builder = ImmutableMap.builder();
        for (E enumVal : enumClass.getEnumConstants()) {
            builder.put(function.apply(enumVal), enumVal);
        }
        return builder.build();
    }
}

