package com.mcintyret.utils.heap;

/**
 * User: mcintyret2
 * Date: 26/02/2013
 */
public interface MaxHeap<T> extends Heap<T> {

    T peekMax();

    T removeMax();

}
