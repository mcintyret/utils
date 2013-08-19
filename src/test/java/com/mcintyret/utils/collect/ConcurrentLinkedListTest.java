package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;
import com.google.common.util.concurrent.Uninterruptibles;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * User: mcintyret2
 * Date: 18/08/2013
 */

@Test
public class ConcurrentLinkedListTest {

    @Test(invocationCount = 100)
    public void shouldAdd() {
        final List<Integer> list = new ConcurrentLinkedList<>();
        final AtomicInteger generator = new AtomicInteger();

        int threads = 10;
        final int perThread = 50;
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch finish = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new Thread() {
                @Override
                public void run() {
                    Uninterruptibles.awaitUninterruptibly(start);
                    try {
                        for (int i = 0; i < perThread; i++) {
                            list.add(generator.getAndIncrement());
                        }
                    } finally {
                        finish.countDown();
                    }

                }
            }.start();
        }

        start.countDown();
        Uninterruptibles.awaitUninterruptibly(finish);

        assertEquals(threads * perThread, list.size());

        for (int i = 0; i < generator.get(); i++) {
            assertTrue("Should contain " + i + " but doesn't", list.contains(i));
        }
    }

    @Test(invocationCount = 100)
    public void shouldClear() {
        final List<Integer> list = new ConcurrentLinkedList<>();
        final AtomicInteger generator = new AtomicInteger();

        int threads = 10;
        final int perThread = 50;
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch finish = new CountDownLatch(threads + 1);
        for (int i = 0; i < threads; i++) {
            new Thread() {
                @Override
                public void run() {
                    Uninterruptibles.awaitUninterruptibly(start);
                    try {
                        for (int i = 0; i < perThread; i++) {
                            list.add(generator.getAndIncrement());
                        }
                    } finally {
                        finish.countDown();
                    }

                }
            }.start();
        }

        new Thread() {
            @Override
            public void run() {
                Uninterruptibles.awaitUninterruptibly(start);
                try {
                    while (finish.getCount() > 5) {
                        Uninterruptibles.sleepUninterruptibly(5, TimeUnit.MILLISECONDS);
                        list.clear();
                    }
                } finally {
                    finish.countDown();
                }
            }
        }.start();

        start.countDown();
        Uninterruptibles.awaitUninterruptibly(finish);

        // Is the list actually the same size that it thinks it is?
        assertEquals(Iterators.size(list.iterator()), list.size());
    }
}
