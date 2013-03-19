package com.mcintyret.utils.collect;

import java.util.*;

/**
 * User: mcintyret2
 * Date: 26/02/2013
 */
public class NoDuplicateList<T> extends AbstractList<T> implements Set<T> {

    private static final int DEFAULT_SIZE = 16;

    private final List<T> list;

    private final Set<T> set;

    public NoDuplicateList() {
        this(DEFAULT_SIZE);
    }

    public NoDuplicateList(Collection<? extends T> c) {
        this(c.size());
        addAll(c);
    }

    public NoDuplicateList(int expectedSize) {
        list = new ArrayList<>(expectedSize);
        set = new HashSet<>(expectedSize);
    }

    @Override
    public boolean add(T t) {
        return set.add(t) && list.add(t);
    }

    @Override
    public void add(int i, T t) {
        if (!set.add(t)) {
            throw new IllegalArgumentException("Cannot add at position " + i + ": already exists in collection.");
        }
        list.add(i, t);
    }

    @Override
    public T set(int i, T t) {
        if (!Objects.equals(t, list.get(i))) {
            if (!set.add(t)) {
                throw new IllegalArgumentException("Cannot set at position " + i + ": already exists in collection.");
            }
            T prev = list.set(i, t);
            set.remove(prev);
            return prev;
        } else {
            return list.set(i, t);
        }
    }

    @Override
    public T get(int i) {
        return list.get(i);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public void clear() {
        list.clear();
        set.clear();
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o) && list.remove(o);
    }

    @Override
    public T remove(int i) {
        T removed = list.remove(i);
        set.remove(removed);
        return removed;
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return new ListIterator<T>() {

            private final ListIterator<T> it;
            private T last;

            {
                it = list.listIterator(index);
            }


            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public T next() {
                last = it.next();
                return last;
            }

            @Override
            public boolean hasPrevious() {
                return it.hasPrevious();
            }

            @Override
            public T previous() {
                last = it.previous();
                return last;
            }

            @Override
            public int nextIndex() {
                return it.nextIndex();
            }

            @Override
            public int previousIndex() {
                return it.previousIndex();
            }

            @Override
            public void remove() {
                it.remove();
                set.remove(last);
            }

            @Override
            public void set(T t) {
                if (!Objects.equals(last, t)) {
                    if (set.add(t)) {
                        it.set(t);
                        set.remove(last);
                    } else {
                        throw new IllegalArgumentException("Cannot set via iterator: already exists in collection.");
                    }
                }
            }

            @Override
            public void add(T t) {
                if (set.add(t)) {
                    it.add(t);
                } else {
                    throw new IllegalArgumentException("Cannot add via iterator: already exists in collection.");
                }
            }
        };
    }
}
