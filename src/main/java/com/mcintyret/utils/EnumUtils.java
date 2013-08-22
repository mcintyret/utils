package com.mcintyret.utils;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

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

    public static <K, V, E extends Enum<E>> Map<E, V> transformMap(Map<K, V> input, Class<E> enumClass, Function<K, E> function) {
        Map<E, V> enumMap = Maps.newEnumMap(enumClass);
        for (Map.Entry<K, V> entry : input.entrySet()) {
            E e = null;
            try {
                e = function.apply(entry.getKey());
            } catch (Exception swallowed) {
                // gulp
            }
            if (e != null) {
                enumMap.put(e, entry.getValue());
            }
        }
        return enumMap;
    }

    public static <E extends Enum<E>> Set<E> setOf(E... elems) {
        Set<E> set = EnumSet.noneOf((Class<E>) elems.getClass().getComponentType());
        for (E elem : elems) {
            set.add(elem);
        }
        return set;
    }

    public static String toName(Enum enumVal) {
        return WordUtils.capitalize(enumVal.name().replaceAll("_", " ").toLowerCase());
    }
}

