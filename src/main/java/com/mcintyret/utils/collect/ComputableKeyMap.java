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

//    protected static class ComputableKeyEntry<K, V> implements Entry<K, V> {
//
//        @Override
//        public K getKey() {
//
//        }
//
//        @Override
//        public V getValue() {
//            return null;  //To change body of implemented methods use File | Settings | File Templates.
//        }
//
//        @Override
//        public V setValue(V value) {
//            return null;  //To change body of implemented methods use File | Settings | File Templates.
//        }
//    }

}
