package com.mcintyret.utils.heap;

/**
 * User: mcintyret2
 * Date: 26/02/2013
 */
public interface MinHeap<T> extends Heap<T> {

    T peekMin();

    T removeMin();

}
