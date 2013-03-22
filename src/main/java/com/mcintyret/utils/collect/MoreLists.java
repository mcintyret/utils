package com.mcintyret.utils.collect;


import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;

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

}
