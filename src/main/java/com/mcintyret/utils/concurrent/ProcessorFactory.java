package com.mcintyret.utils.concurrent;


import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 02/04/2013
 */
public interface ProcessorFactory<T> {

    Runnable newRunnable(T next);

    Iterable<T> getIterable();

    void beforeExecution();

    void afterExecution();

    boolean shouldExecute(T next);

    void onCancellation(Iterator<T> iterator);
}
