package com.mcintyret.utils.collect;


import com.google.common.base.Predicate;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public final class MoreLists {

    private MoreLists() {

    }

    public static <T> List<T> removeDuplicates(List<T> list) {
        return Lists.newArrayList(Sets.newLinkedHashSet(list));
    }

    public static <T> List<T> reverse(List<T> list) {
        return Lists.newArrayList(Lists.reverse(list));
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return Lists.newArrayList(Iterables.filter(list, predicate));
    }

    public static <T> List<T> limitedList(int maxSize) {
        return limitedList(maxSize, Lists.<T>newArrayList());
    }

    public static <T> List<T> limitedList(final int maxSize, final List<T> backingList) {
        checkArgument(backingList.isEmpty(), "Must provide an empty list to limitedList");
        return new ForwardingList<T>() {
            @Override
            protected List<T> delegate() {
                return backingList;
            }

            @Override
            public boolean add(T element) {
                return size() < maxSize && backingList.add(element);
            }

            @Override
            public boolean addAll(Collection<? extends T> c) {
                return size() + c.size() <= maxSize && backingList.addAll(c);
            }

            @Override
            public void add(int index, T element) {
                if (size() < maxSize) {
                    backingList.add(index, element);
                }
            }

            @Override
            public boolean addAll(int index, Collection<? extends T> c) {
                return size() + c.size() <= maxSize && backingList.addAll(index, c);
            }
        };
    }

    public static <C extends Comparable<C>> int addToSortedList(List<C> list, C toAdd) {
        int index = Collections.binarySearch(list, toAdd);
        if (index < 0) {
            index = -(index + 1);
        }
        list.add(index, toAdd);
        return index;
    }

}
