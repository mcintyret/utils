package com.mcintyret.utils;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * User: mcintyret2
 * Date: 02/04/2013
 */
public class ThreadedForEach<T> {

    private final Iterable<T> iterable;
    private final ProcessorFactory<T> factory;

    private final ExecutorService executor;

    private final Map<T, Future<?>> futures;

    public ThreadedForEach(ProcessorFactory<T> processorFactory, ExecutorService executor) {
        this.iterable = processorFactory.getIterable();
        this.factory = processorFactory;
        this.executor = executor;
        if (iterable instanceof Collection) {
            futures = Maps.newHashMapWithExpectedSize(((Collection) iterable).size());
        } else {
            futures = Maps.newHashMap();
        }
    }

    public ThreadedForEach(ProcessorFactory<T> processorFactory) {
        this(processorFactory, Executors.newFixedThreadPool(10));
    }

    private volatile boolean running = false;

    public final void execute() {
        factory.beforeExecution();
        Iterator<T> iterator  = iterable.iterator();

        running = true;
        while(running && iterator.hasNext()) {
            T next = iterator.next();
            if (factory.shouldExecute(next)) {
                futures.put(next, executor.submit(factory.newRunnable(next)));
            }
        }
        stop();
        executor.shutdown();

        factory.afterExecution();
    }

    public final void cancel(T t) {
        Future<?> f = futures.get(t);
        if (f != null) {
            f.cancel(true);
        }
    }

    public final void cancelAll() {
        stop();
        executor.shutdownNow();
    }

    public final void stop() {
        running = false;
    }

    public final boolean isTerminated() {
        return executor.isTerminated();
    }

    public final boolean isRunning() {
        return running;
    }

    public final boolean awaitTermination(long timeoutMillis) {
        try {
            return executor.awaitTermination(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
