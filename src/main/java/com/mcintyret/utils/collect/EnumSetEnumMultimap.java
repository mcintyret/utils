package com.mcintyret.utils.collect;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * User: tommcintyre
 * Date: 8/22/13
 */
public class EnumSetEnumMultimap<E extends Enum<E>, V extends Enum<V>> extends AbstractSetEnumMultimap<E, V> {

    private final Class<V> enumValueClass;

    public EnumSetEnumMultimap(Class<E> enumClass, Class<V> enumValueClass) {
        super(enumClass);
        this.enumValueClass = enumValueClass;
    }

    @Override
    protected Set<V> newCollection() {
        return EnumSet.noneOf(enumValueClass);
    }
}
