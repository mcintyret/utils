//package com.mcintyret.utils.collect;
//
//import com.google.common.util.concurrent.SettableFuture;
//
//import java.util.Collection;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
///**
// * User: mcintyret2
// * Date: 12/05/2013
// */
//public class BlockingMap<K, V> implements Map<K, V> {
//
//    private final ConcurrentMap<K, SettableFuture<V>> delegate;
//
//    public BlockingMap(int initialSize) {
//        this.delegate = new ConcurrentHashMap<>(initialSize);
//    }
//
//    public BlockingMap() {
//        this.delegate = new ConcurrentHashMap<>();
//    }
//
//    @Override
//    public int size() {
//        return delegate.size();
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return delegate.isEmpty();
//    }
//
//    @Override
//    public boolean containsKey(Object key) {
//        return
//    }
//
//    @Override
//    public boolean containsValue(Object value) {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public V get(Object key) {
//        return MapUtils.putOnce(delegate, (K) key, FutureLoader.INSTANCE).get();
//    }
//
//    @Override
//    public V put(K key, V value) {
//        SettableFuture<V> future = MapUtils.putOnce(delegate, key, FutureLoader.INSTANCE);
//        if (future.isDone()) {
//            throw new IllegalArgumentException("Cannot put on the same key multiple times");
//        }
//        future.set(value);
//        return null;
//    }
//
//    @Override
//    public V remove(Object key) {
//        SettableFuture<V> future = delegate.remove(key);
//        return future == null || !future.isDone() ? null : future.get();
//    }
//
//    @Override
//    public void putAll(Map<? extends K, ? extends V> m) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void clear() {
//        delegate.clear();
//    }
//
//    @Override
//    public Set<K> keySet() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public Collection<V> values() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public Set<Entry<K, V>> entrySet() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    private enum FutureLoader implements Loader<SettableFuture<V>>  {
//        INSTANCE;
//
//        @Override
//        public SettableFuture<V> load() {
//            return SettableFuture.create();
//        }
//    }
//
//}
