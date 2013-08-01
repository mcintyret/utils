package com.mcintyret.utils.filter;

import com.google.common.base.Objects;

/**
 * User: mcintyret2
 * Date: 01/08/2013
 */
public class EqualsFilter<T> extends AbstractFilter<T> {

    private T value;

    public EqualsFilter(T value) {
        this.value = value;
    }

    @Override
    public boolean apply(T t) {
        return Objects.equal(t, value);
    }

    public void setValue(T value) {
        if (!Objects.equal(this.value, value)) {
            this.value = value;
            setState(State.UNKNOWN);
        }
    }
}
