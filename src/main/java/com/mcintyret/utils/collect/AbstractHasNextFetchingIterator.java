package com.mcintyret.utils.collect;

import java.util.NoSuchElementException;

/**
 * User: mcintyret2
 * Date: 20/03/2013
 */
public abstract class AbstractHasNextFetchingIterator<T> extends AbstractRemovingIterator<T> {

    private T next;

     @Override
    public boolean hasNext() {
         return next != null || doHasNext();
     }

    protected abstract boolean doHasNext();

    @Override
    protected final T doNext() {
        if (hasNext()) {
            T temp = next;
            next = null;
            return temp;
        }
        throw new NoSuchElementException();
    }

    protected void setNext(T next) {
        this.next = next;
    }
}
