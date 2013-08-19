package com.mcintyret.utils.filter.adapter;

import com.mcintyret.utils.filter.Filter;

/**
 * User: mcintyret2
 * Date: 12/08/2013
 */
public abstract class AndMultiFilterAdapter<A, B> extends AbstractFilterAdapter<A, B> {

    public AndMultiFilterAdapter(Filter<A> filter) {
        super(filter);
    }

    @Override
    public final boolean apply(B b) {
        for (A a : transform(b)) {
            if (!filter.apply(a)) {
                return false;
            }
        }
        return true;
    }

    protected abstract Iterable<A> transform(B b);
}
