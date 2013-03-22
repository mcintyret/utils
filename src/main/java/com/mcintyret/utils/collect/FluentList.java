package com.mcintyret.utils.collect;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public abstract class FluentList<E> extends ForwardingList<E> {

    public static <T> FluentList<T> ofList(final List<T> list) {
        return list instanceof FluentList ? (FluentList<T>) list :
                new FluentList<T>() {
                    @Override
                    protected List<T> delegate() {
                        return list;
                    }
                };
    }

    public FluentList<E> sort() {
        Collections.sort((List<Comparable>) this);
        return this;
    }

    public FluentList<E> sort(Comparator<E> comparator) {
        Collections.sort(this, comparator);
        return this;
    }

    public FluentList<E> reverse() {
        return ofList(Lists.newArrayList(Lists.reverse(this)));
    }

    public FluentList<E> removeDuplicates() {
        return ofList(MoreLists.removeDuplicates(this));
    }

    public FluentList<E> filter(Predicate<E> predicate) {
        return ofList(MoreLists.filter(this, predicate));
    }

    public <T> FluentList<T> transform(Function<? super E, ? extends T> function) {
        return ofList(Lists.transform(this, function));
    }

}
