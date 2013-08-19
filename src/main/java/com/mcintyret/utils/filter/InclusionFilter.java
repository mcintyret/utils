package com.mcintyret.utils.filter;

import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * User: mcintyret2
 * Date: 01/08/2013
 */
public class InclusionFilter<T> extends AbstractFilter<T> {

    private final Set<T> set;

    public InclusionFilter(Set<T> set) {
        this.set = set;
    }

    public InclusionFilter(T... initial) {
        this(new HashSet<T>());
        Collections.addAll(set, initial);
    }

    @Override
    public boolean apply(T t) {
        return set.contains(t);
    }

    public void add(T t) {
        if (set.add(t)) {
            expand();
        }
    }

    public void addAll(Iterable<T> iterable) {
        if (Iterables.addAll(set, iterable)) {
            expand();
        }
    }

    public void clear() {
        if (!set.isEmpty()) {
            set.clear();
            restrict();
        }
    }

    public void remove(T t) {
        if (set.remove(t)) {
            restrict();
        }
    }

    public void removeAll(Collection<T> c) {
        if (set.removeAll(c)) {
            restrict();
        }
    }

    public void set(Collection<T> c) {
        Set<T> copy = new HashSet<>(set);
        copy.removeAll(c);
        removeAll(copy);
        addAll(c);
    }
}
