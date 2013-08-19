package com.mcintyret.utils.filter;

/**
 * User: mcintyret2
 * Date: 06/08/2013
 */
abstract class BaseFilter<T> implements Filter<T> {

    boolean cleared = false;

    @Override
    public final State getState() {
        return cleared ? doGetState() : State.EXPANDED;
    }

    protected abstract State doGetState();

    @Override
    public final void clearState() {
        cleared = true;
        doClearState();
    }

    protected abstract void doClearState();

}
