package com.mcintyret.utils.collect;

/**
 * User: mcintyret2
 * Date: 10/04/2013
 */
/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;

import javax.annotation.Nullable;
import java.util.*;

import static com.mcintyret.utils.collect.MapUtils.keyOrNull;
import static com.mcintyret.utils.collect.MoreIterators.pollNext;

/**
 * Skeletal implementation of {@link java.util.NavigableMap}.
 *
 * @author Louis Wasserman
 */
abstract class AbstractNavigableMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V> {

    @Override
    @Nullable
    public abstract V get(@Nullable Object key);

    @Override
    @Nullable
    public Entry<K, V> firstEntry() {
        return Iterators.getNext(entryIterator(), null);
    }

    @Override
    @Nullable
    public Entry<K, V> lastEntry() {
        return Iterators.getNext(descendingEntryIterator(), null);
    }

    @Override
    @Nullable
    public Entry<K, V> pollFirstEntry() {
        return pollNext(entryIterator());
    }

    @Override
    @Nullable
    public Entry<K, V> pollLastEntry() {
        return pollNext(descendingEntryIterator());
    }

    @Override
    public K firstKey() {
        Entry<K, V> entry = firstEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        } else {
            return entry.getKey();
        }
    }

    @Override
    public K lastKey() {
        Entry<K, V> entry = lastEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        } else {
            return entry.getKey();
        }
    }

    @Override
    @Nullable
    public Entry<K, V> lowerEntry(K key) {
        return headMap(key, false).lastEntry();
    }

    @Override
    @Nullable
    public Entry<K, V> floorEntry(K key) {
        return headMap(key, true).lastEntry();
    }

    @Override
    @Nullable
    public Entry<K, V> ceilingEntry(K key) {
        return tailMap(key, true).firstEntry();
    }

    @Override
    @Nullable
    public Entry<K, V> higherEntry(K key) {
        return tailMap(key, false).firstEntry();
    }

    @Override
    public K lowerKey(K key) {
        return keyOrNull(lowerEntry(key));
    }

    @Override
    public K floorKey(K key) {
        return keyOrNull(floorEntry(key));
    }

    @Override
    public K ceilingKey(K key) {
        return keyOrNull(ceilingEntry(key));
    }

    @Override
    public K higherKey(K key) {
        return keyOrNull(higherEntry(key));
    }

    abstract Iterator<Entry<K, V>> entryIterator();

    abstract Iterator<Entry<K, V>> descendingEntryIterator();

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        return new NavigableMapSetView<>(this);
    }

    @Override
    public Set<K> keySet() {
        return navigableKeySet();
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return subMap(firstKey(), true, toKey, inclusive);
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return subMap(fromKey, inclusive, lastKey(), inclusive);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return entryIterator();
            }

            @Override
            public int size() {
                return AbstractNavigableMap.this.size();
            }
        };
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        return new DescendingMap<K, V>() {

            @Override
            protected NavigableMap<K, V> forward() {
                return AbstractNavigableMap.this;
            }

            @Override
            protected Iterator<Entry<K, V>> entryIterator() {
                return descendingEntryIterator();
            }
        };
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    protected static abstract class DescendingMap<K, V> extends ForwardingMap<K, V> implements NavigableMap<K, V> {

        protected abstract NavigableMap<K, V> forward();

        protected abstract Iterator<Entry<K, V>> entryIterator();

        @Override
        protected final Map<K, V> delegate() {
            return forward();
        }

        private transient Comparator<? super K> comparator;

        @SuppressWarnings("unchecked")
        @Override
        public Comparator<? super K> comparator() {
            Comparator<? super K> result = comparator;
            if (result == null) {
                Comparator<? super K> forwardCmp = forward().comparator();
                if (forwardCmp == null) {
                    forwardCmp = (Comparator) Ordering.natural();
                }
                result = comparator = reverse(forwardCmp);
            }
            return result;
        }

        // If we inline this, we get a javac error.
        private <T> Ordering<T> reverse(Comparator<T> forward) {
            return Ordering.from(forward).reverse();
        }

        @Override
        public K firstKey() {
            return forward().lastKey();
        }

        @Override
        public K lastKey() {
            return forward().firstKey();
        }

        @Override
        public Entry<K, V> lowerEntry(K key) {
            return forward().higherEntry(key);
        }

        @Override
        public K lowerKey(K key) {
            return forward().higherKey(key);
        }

        @Override
        public Entry<K, V> floorEntry(K key) {
            return forward().ceilingEntry(key);
        }

        @Override
        public K floorKey(K key) {
            return forward().ceilingKey(key);
        }

        @Override
        public Entry<K, V> ceilingEntry(K key) {
            return forward().floorEntry(key);
        }

        @Override
        public K ceilingKey(K key) {
            return forward().floorKey(key);
        }

        @Override
        public Entry<K, V> higherEntry(K key) {
            return forward().lowerEntry(key);
        }

        @Override
        public K higherKey(K key) {
            return forward().lowerKey(key);
        }

        @Override
        public Entry<K, V> firstEntry() {
            return forward().lastEntry();
        }

        @Override
        public Entry<K, V> lastEntry() {
            return forward().firstEntry();
        }

        @Override
        public Entry<K, V> pollFirstEntry() {
            return forward().pollLastEntry();
        }

        @Override
        public Entry<K, V> pollLastEntry() {
            return forward().pollFirstEntry();
        }

        @Override
        public NavigableMap<K, V> descendingMap() {
            return forward();
        }

        private transient Set<Entry<K, V>> entrySet;

        @Override
        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> result = entrySet;
            return (result == null) ? entrySet = createEntrySet() : result;
        }

        Set<Entry<K, V>> createEntrySet() {
            return new AbstractSet<Entry<K, V>>() {

                @Override
                public Iterator<Entry<K, V>> iterator() {
                    return entryIterator();
                }

                @Override
                public int size() {
                    return DescendingMap.this.size();
                }
            };
        }

        @Override
        public Set<K> keySet() {
            return navigableKeySet();
        }

        private transient NavigableSet<K> navigableKeySet;

        @Override
        public NavigableSet<K> navigableKeySet() {
            NavigableSet<K> result = navigableKeySet;
            return (result == null) ? navigableKeySet = new NavigableMapSetView<>(this) : result;
        }

        @Override
        public NavigableSet<K> descendingKeySet() {
            return forward().navigableKeySet();
        }

        @Override
        public NavigableMap<K, V>
        subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
        }

        @Override
        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return forward().tailMap(toKey, inclusive).descendingMap();
        }

        @Override
        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return forward().headMap(fromKey, inclusive).descendingMap();
        }

        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        @Override
        public SortedMap<K, V> headMap(K toKey) {
            return headMap(toKey, false);
        }

        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            return tailMap(fromKey, true);
        }

        @Override
        public Collection<V> values() {
            return MapUtils.values(this);
        }

        @Override
        public String toString() {
            Iterator<Entry<K, V>> i = entrySet().iterator();
            if (!i.hasNext())
                return "{}";

            StringBuilder sb = new StringBuilder();
            sb.append('{');
            for (; ; ) {
                Entry<K, V> e = i.next();
                K key = e.getKey();
                V value = e.getValue();
                sb.append(key == this ? "(this Map)" : key);
                sb.append('=');
                sb.append(value == this ? "(this Map)" : value);
                if (!i.hasNext())
                    return sb.append('}').toString();
                sb.append(',').append(' ');
            }
        }
    }

}
