package com.mcintyret.utils;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: mcintyret2
 * Date: 02/04/2013
 */
public class ThreadedForEach<T> {

    private final Iterable<T> iterable;
    private final ProcessorFactory<T> factory;

    private final ExecutorService executor;

    private final Map<T, Future<?>> futures;


    public ThreadedForEach(Iterable<T> iterable, ProcessorFactory<T> processorFactory, ExecutorService executor) {
        this.iterable = iterable;
        this.factory = processorFactory;
        this.executor = executor;
        if (iterable instanceof Collection) {
            futures = Maps.newHashMapWithExpectedSize(((Collection) iterable).size());
        } else {
            futures = Maps.newHashMap();
        }
    }

    public ThreadedForEach(Iterable<T> iterable, ProcessorFactory<T> processorFactory) {
        this(iterable, processorFactory, Executors.newFixedThreadPool(5));
    }

    private volatile boolean running = false;

    public final void execute() {
        Iterator<T> iterator  = iterable.iterator();
        while(running && iterator.hasNext()) {
            T next = iterator.next();
            futures.put(next, executor.submit(factory.newProcessor(next, this)));
        }
        executor.shutdown();
    }

    public void cancel(T t) {
        Future<?> f = futures.get(t);
        if (f != null) {
            f.cancel(true);
        }
    }

    public void cancelAll() {
        executor.shutdownNow();
    }

    public final void stop() {
        running = false;
    }

    public final boolean isShutDown() {
        return executor.isShutdown();
    }
}
