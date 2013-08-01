package com.mcintyret.utils.filter;

/**
 * User: mcintyret2
 * Date: 31/07/2013
 */
public abstract class AbstractFilter<T> implements Filter<T> {

    private State state = State.EXPANDED;

    @Override
    public State getState() {
        return state;
    }

    @Override
    public final void clearState() {
        setState(State.NO_CHANGE);
    }

    @Override
    public Filter<T> negate() {
        return new NegatedFilter<>(this);
    }

    protected void setState(State state) {
        this.state = state;
    }

    protected void expand() {
        switch (getState()) {
            case NO_CHANGE:
                setState(State.EXPANDED);
                return;
            case RESTRICTED:
                setState(State.RESTRICTED);
                return;
            default:
                return;
        }
    }

    protected void restrict() {
        switch (getState()) {
            case NO_CHANGE:
                setState(State.RESTRICTED);
                return;
            case EXPANDED:
                setState(State.UNKNOWN);
                return;
            case RESTRICTED:
            case UNKNOWN:
                return;
        }
    }

}
