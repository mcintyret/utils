package com.mcintyret.utils.collect;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public class AtomicSizer implements Sizer {

    private final AtomicInteger size = new AtomicInteger();

    @Override
    public void modifySize(int delta) {
        size.addAndGet(delta);
    }

    @Override
    public void resetSize() {
        size.set(0);
    }

    @Override
    public int getSize() {
        return size.get();
    }
}
