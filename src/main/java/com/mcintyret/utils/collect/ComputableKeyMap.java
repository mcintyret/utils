package com.mcintyret.utils.collect;

import com.google.common.base.Function;

import java.util.AbstractMap;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public abstract class ComputableKeyMap<K, V> extends AbstractMap<K, V> {

    private final Function<V, K> keyFromValueFunction;

    protected ComputableKeyMap(Function<V, K> keyFromValueFunction) {
        this.keyFromValueFunction = keyFromValueFunction;
    }

    protected K valueToKey(Object value) {
        return keyFromValueFunction.apply((V) value);
    }

    protected class ComputableKeyEntry implements Entry<K, V> {

        private V value;

        protected ComputableKeyEntry(V value) {
            this.value = value;
        }

        @Override
        public K getKey() {
            return keyFromValueFunction.apply(value);
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldVal = this.value;
            this.value = value;
            return value;
        }
    }

}
