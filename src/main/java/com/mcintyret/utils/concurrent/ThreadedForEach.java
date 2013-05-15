package com.mcintyret.utils.concurrent;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Collections.synchronizedSet;

/**
 * User: mcintyret2
 * Date: 02/04/2013
 */
public class ThreadedForEach<T> {

    private final Iterable<T> iterable;
    private final ProcessorFactory<T> factory;

    private final ExecutorService executor;

    private final Map<T, Future<?>> futures;

    private final Set<T> inProgress = synchronizedSet(new LinkedHashSet<T>());

    public ThreadedForEach(ProcessorFactory<T> processorFactory, ExecutorService executor) {
        this.iterable = processorFactory.getIterable();
        this.factory = processorFactory;
        if (factory instanceof ThreadedForEachAwareProcessorFactory) {
            ((ThreadedForEachAwareProcessorFactory) factory).setTfe(this);
        }
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

    private final AtomicBoolean run = new AtomicBoolean(false);

    public final void execute() {
        execute(true);
    }

    public final void execute(boolean blocking) {
        if (!run.getAndSet(true)) {
            factory.beforeExecution();
            Iterator<T> iterator = iterable.iterator();
            final Phaser phaser = new Phaser(1);

            running = true;
            while (running && iterator.hasNext()) {
                final T next = iterator.next();
                if (factory.shouldExecute(next)) {
                    inProgress.add(next);
                    phaser.register();
                    futures.put(next, executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                factory.newRunnable(next).run();
                            } finally {
                                inProgress.remove(next);
                                phaser.arriveAndDeregister();
                            }
                        }
                    }));
                }
            }
            stop();
            if (blocking) {
                phaser.arriveAndAwaitAdvance();
                if (iterator.hasNext() || !inProgress.isEmpty()) {
                    factory.onCancellation(Iterators.concat(iterator, inProgress.iterator()));
                }

                factory.afterExecution();
            }
        } else {
            throw new IllegalStateException("This ThreadedForEach has already run!");
        }
    }

    public final void stop() {
        running = false;
    }

    public final boolean isRunning() {
        return running;
    }
}
