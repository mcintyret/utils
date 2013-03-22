package com.mcintyret.utils.collect;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public class SimpleSizer implements Sizer {

    private int size;

    @Override
    public void modifySize(int delta) {
        size += delta;
    }

    @Override
    public void resetSize() {
        size = 0;
    }

    @Override
    public int getSize() {
        return size;
    }
}
