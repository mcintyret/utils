package com.mcintyret.utils;

import java.util.Collections;
import java.util.EnumSet;
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
        return Collections.unmodifiableSet(set);
    }
}

