package com.mcintyret.utils.collect;

import java.util.HashSet;
import java.util.Set;

/**
 * User: tommcintyre
 * Date: 8/22/13
 */
public class HashSetEnumMultimap<E extends Enum<E>, V> extends AbstractSetEnumMultimap<E, V> {

    public HashSetEnumMultimap(Class<E> enumClass) {
        super(enumClass);
    }

    @Override
    protected Set<V> newCollection() {
        return new HashSet<>();
    }

}
