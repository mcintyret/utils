package com.mcintyret.utils.filter;

/**
 * User: mcintyret2
 * Date: 31/07/2013
 */
public class NegatedFilter<T> extends BaseFilter<T> {

    private final Filter<T> delegate;

    public NegatedFilter(Filter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    protected State doGetState() {
        switch (delegate.getState()) {
            case NO_CHANGE:
                return State.NO_CHANGE;
            case EXPANDED:
                return State.RESTRICTED;
            case RESTRICTED:
                return State.EXPANDED;
            case UNKNOWN:
                return State.UNKNOWN;
            default:
                throw new AssertionError();
        }
    }

    @Override
    protected void doClearState() {
        delegate.clearState();
    }

    @Override
    public boolean apply(T t) {
        return !delegate.apply(t);
    }

    @Override
    public Filter<T> negate() {
        return delegate;
    }
}
