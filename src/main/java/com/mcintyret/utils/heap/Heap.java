package com.mcintyret.utils.heap;

import java.util.Collection;

/**
 * User: mcintyret2
 * Date: 26/02/2013
 */
public interface Heap<T> extends Collection<T> {

    boolean add(T t);

    void merge(Heap<T> heap);

    boolean isFull();


}
