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
public class ConcurrentQuickAppendListTest {

    @Test(invocationCount = 100)
    public void shouldAdd() {
        final List<Integer> list = new ConcurrentQuickAppendList<>();
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
    public void shouldAddAll() {
        final List<Integer> superList = new ConcurrentQuickAppendList<>();

        int threads = 10;
        final int perThread = 50;
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch finish = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            final int base = i * perThread;
            new Thread() {
                @Override
                public void run() {
                    Uninterruptibles.awaitUninterruptibly(start);
                    List<Integer> list = new ConcurrentQuickAppendList<>();
                    try {
                        for (int i = 0; i < perThread; i++) {
                            list.add(base + i);
                        }
                        superList.addAll(list);
                    } finally {
                        finish.countDown();
                    }

                }
            }.start();
        }

        start.countDown();
        Uninterruptibles.awaitUninterruptibly(finish);

        int size = superList.size();
        assertEquals(threads * perThread, size);

        for (int i = 0; i < size; i++) {
            assertTrue("Should contain " + i + " but doesn't", superList.contains(i));
        }

        // Now test that each ConcurrentQuickAppendList added with addAll is added contiguously
        int index = 0;
        int prev = -435738967;
        for (int val : superList) {
            if (index++ % perThread != 0) {
                assertEquals(prev + 1, val);
            }
            prev = val;
        }
    }

    @Test(invocationCount = 100)
    public void shouldClear() {
        final List<Integer> list = new ConcurrentQuickAppendList<>();
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
