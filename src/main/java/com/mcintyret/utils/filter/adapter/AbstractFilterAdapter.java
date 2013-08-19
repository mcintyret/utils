package com.mcintyret.utils.filter.adapter;

import com.mcintyret.utils.filter.Filter;
import com.mcintyret.utils.filter.NegatedFilter;

/**
 * User: mcintyret2
 * Date: 12/08/2013
 */
public abstract class AbstractFilterAdapter<A, B> implements Filter<B> {

    protected final Filter<A> filter;

    public AbstractFilterAdapter(Filter<A> filter) {
        this.filter = filter;
    }

    @Override
    public final State getState() {
        return filter.getState();
    }

    @Override
    public final void clearState() {
        filter.clearState();
    }

    @Override
    public final Filter<B> negate() {
        return new NegatedFilter<>(this);
    }

}
