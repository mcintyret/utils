package com.mcintyret.utils.collect;

import com.google.common.collect.ForwardingList;

import java.util.*;

/**
 * User: mcintyret2
 * Date: 14/05/2013
 */
public class SortedList<C extends Comparable<C>> extends ForwardingList<C> implements RandomAccess {

    private final List<C> delegate;

    public SortedList() {
        this(16);
    }

    public SortedList(int size) {
        delegate = new ArrayList<>(size);
    }

    public SortedList(Collection<? extends C> c) {
        this(c.size());
        addAll(c);
    }


    @Override
    protected List<C> delegate() {
        return delegate;
    }

    @Override
    public boolean addAll(Collection<? extends C> collection) {
        boolean changed = false;
        for (C c : collection) {
            changed |= add(c);
        }
        return changed;
    }

    @Override
    public C set(int i, C c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int i, C c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(C element) {
        MoreLists.addToSortedList(delegate, element);
        return true;
    }
}
