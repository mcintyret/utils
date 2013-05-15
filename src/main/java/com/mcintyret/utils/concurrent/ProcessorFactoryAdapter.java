package com.mcintyret.utils.concurrent;

import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 05/04/2013
 */
public abstract class ProcessorFactoryAdapter<T> implements ProcessorFactory<T> {

    @Override
    public void beforeExecution() {
        // do nothing by default
    }

    @Override
    public void afterExecution() {
        // do nothing by default
    }

    @Override
    public boolean shouldExecute(T next) {
        return true; // Execute by default
    }

    @Override
    public void onCancellation(Iterator<T> remaining) {
        // do nothing by default
    }

}
