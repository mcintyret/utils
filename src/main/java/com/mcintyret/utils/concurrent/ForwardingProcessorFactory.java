package com.mcintyret.utils.concurrent;

import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 14/05/2013
 */
public abstract class ForwardingProcessorFactory<T> implements ProcessorFactory<T> {

    protected abstract ProcessorFactory<T> delegate();

    @Override
    public Runnable newRunnable(T next) {
        return delegate().newRunnable(next);
    }

    @Override
    public Iterable<T> getIterable() {
        return delegate().getIterable();
    }

    @Override
    public void beforeExecution() {
        delegate().beforeExecution();
    }

    @Override
    public void afterExecution() {
        delegate().afterExecution();
    }

    @Override
    public boolean shouldExecute(T next) {
        return delegate().shouldExecute(next);
    }

    @Override
    public void onCancellation(Iterator<T> iterator) {
        delegate().onCancellation(iterator);
    }
}
