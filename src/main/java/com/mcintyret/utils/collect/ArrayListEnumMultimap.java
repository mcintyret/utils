package com.mcintyret.utils.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: tommcintyre
 * Date: 8/22/13
 */
public class ArrayListEnumMultimap<E extends Enum<E>, V> extends AbstractEnumMultimap<E, V, List<V>> {

    public ArrayListEnumMultimap(Class<E> enumClass) {
        super(enumClass);
    }

    @Override
    protected List<V> newCollection() {
        return new ArrayList<>();
    }

    @Override
    protected List<V> emptyCollection() {
        return Collections.emptyList();
    }
}
