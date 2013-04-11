package com.mcintyret.utils;

/**
 * User: mcintyret2
 * Date: 05/04/2013
 */
public abstract class AbstractProcessorFactory<T> implements ProcessorFactory<T> {

    public void beforeExecution() {
        // do nothing by default
    }

    public void afterExecution() {
        // do nothing by default
    }

    public boolean shouldExecute(T next) {
        return true; // Execute by default
    }

}
