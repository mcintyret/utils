package com.mcintyret.utils.filter;

/**
 * User: mcintyret2
 * Date: 31/07/2013
 */
public interface Filter<T> {

    public enum State {
        NO_CHANGE,
        EXPANDED,
        RESTRICTED,
        UNKNOWN
    }

    State getState();

    void clearState();

    boolean apply(T t);

    Filter<T> negate();
}
