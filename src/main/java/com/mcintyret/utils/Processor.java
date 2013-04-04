package com.mcintyret.utils;

/**
 * User: mcintyret2
 * Date: 02/04/2013
 */
public abstract class Processor<T> implements Runnable {

    private final T t;
    private final ThreadedForEach<T> tfe;

    public Processor(T t, ThreadedForEach<T> tfe) {
        this.t = t;
        this.tfe = tfe;
    }

    protected T getArg() {
        return t;
    }

    protected void stop() {
        tfe.stop();
    }

}
