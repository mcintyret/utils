package com.mcintyret.utils.concurrent;

/**
 * User: mcintyret2
 * Date: 14/05/2013
 */
public abstract class ThreadedForEachAwareProcessorFactory<T> extends ProcessorFactoryAdapter<T> {

    private ThreadedForEach<T> tfe;

    protected void stop() {
        tfe.stop();
    }

    void setTfe(ThreadedForEach<T> tfe) {
        this.tfe = tfe;
    }
}
