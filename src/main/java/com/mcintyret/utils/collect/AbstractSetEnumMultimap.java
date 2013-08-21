package com.mcintyret.utils.collect;

import java.util.Collections;
import java.util.Set;

/**
 * User: tommcintyre
 * Date: 8/22/13
 */
public abstract class AbstractSetEnumMultimap<E extends Enum<E>, V> extends AbstractEnumMultimap<E, V, Set<V>> {

    public AbstractSetEnumMultimap(Class<E> enumClass) {
        super(enumClass);
    }

    @Override
    protected Set<V> emptyCollection() {
        return Collections.emptySet();
    }
}
