package com.mcintyret.utils.collect;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.util.concurrent.SettableFuture;

import java.util.*;
import java.util.concurrent.*;

/**
 * User: mcintyret2
 * Date: 12/05/2013
 */
public class BlockingMap<K, V> implements Map<K, V> {

    private final ConcurrentMap<K, SettableFuture<V>> delegate;

    public BlockingMap(int initialSize) {
        this.delegate = new ConcurrentHashMap<>(initialSize);
    }

    public BlockingMap() {
        this.delegate = new ConcurrentHashMap<>();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        Future<V> future = delegate.get(key);
        return future != null && future.isDone();
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        return getUnchecked(getOrPutFuture(key));
    }

    public V getInterruptibly(Object key) throws ExecutionException, InterruptedException {
        return getOrPutFuture(key).get();
    }

    public V getInterruptibly(Object key, long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return getOrPutFuture(key).get(timeout, unit);
    }

    @Override
    public V put(K key, V value) {
        SettableFuture<V> future = getOrPutFuture(key);
        V old = future.isDone() ? getUnchecked(future) : null;
        future.set(value);
        return old;
    }

    @Override
    public V remove(Object key) {
        SettableFuture<V> future = delegate.remove(key);
        return future == null ? null : getUnchecked(future);
    }

    public V removeInterruptibly(Object key) throws ExecutionException, InterruptedException {
        SettableFuture<V> future = delegate.remove(key);
        return future == null ? null : future.get();
    }

    public V removeInterruptibly(Object key, long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        SettableFuture<V> future = delegate.remove(key);
        return future == null ? null : future.get(timeout, unit);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
             put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<K> keySet() {
        return MapUtils.keySet(this);
    }

    @Override
    public Collection<V> values() {
        return MapUtils.values(this);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return Iterators.transform(Iterators.filter(delegate.entrySet().iterator(), new Predicate<Entry<K, SettableFuture<V>>>() {
                    @Override
                    public boolean apply(Entry<K, SettableFuture<V>> input) {
                        return input.getValue().isDone();
                    }
                }), new Function<Entry<K, SettableFuture<V>>, Entry<K, V>>() {
                    @Override
                    public Entry<K, V> apply(final Entry<K, SettableFuture<V>> input) {
                        return new Entry<K, V>() {

                            @Override
                            public K getKey() {
                                return input.getKey();
                            }

                            @Override
                            public V getValue() {
                                return getUnchecked(input.getValue());
                            }

                            @Override
                            public V setValue(V value) {
                                V old = getValue();
                                input.getValue().set(value);
                                return old;
                            }
                        };
                    }
                });
            }

            @Override
            public int size() {
                return Iterators.size(iterator());
            }
        };
    }

    private enum FutureLoader implements Loader<SettableFuture<?>> {
        INSTANCE;

        @Override
        public SettableFuture<?> load() {
            return SettableFuture.create();
        }
    }

    private static <T> T getUnchecked(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            return null;
        }
    }

    private SettableFuture<V> getOrPutFuture(Object key) {
        return MapUtils.putOnce(delegate, (K) key, (Loader<SettableFuture<V>>) FutureLoader.INSTANCE);
    }

}
