package com.mcintyret.utils.collect;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: tommcintyre
 * Date: 8/22/13
 */
public class TreeSetEnumMultimap<E extends Enum<E>, V> extends AbstractSetEnumMultimap<E, V> {

    public TreeSetEnumMultimap(Class<E> enumClass) {
        super(enumClass);
    }

    @Override
    protected Set<V> newCollection() {
        return new TreeSet<>();
    }
}
