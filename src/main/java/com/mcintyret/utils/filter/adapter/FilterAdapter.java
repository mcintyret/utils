package com.mcintyret.utils.filter.adapter;

import com.mcintyret.utils.filter.Filter;

/**
 * User: mcintyret2
 * Date: 02/08/2013
 */
public abstract class FilterAdapter<A, B> extends AbstractFilterAdapter<A, B> {

    public FilterAdapter(Filter<A> filter) {
        super(filter);
    }

    @Override
    public final boolean apply(B b) {
        return filter.apply(transform(b));
    }

    protected abstract A transform(B b);
}
