package com.mcintyret.utils.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: mcintyret2
 * Date: 18/08/2013
 */
public class ConcurrentQuickAppendList<T> extends AbstractList<T> {

    private final AtomicInteger size = new AtomicInteger();

    private final AtomicReference<Node<T>> head = new AtomicReference<>();

    private final AtomicReference<Node<T>> tail = new AtomicReference<>();

    @Override
    public boolean add(T elem) {
        addNodeToTail(new Node<>(elem), 1);
        return true;
    }

    private void addNodeToTail(Node<T> newNode, int sizeChange) {
        addNodeToTail(newNode, newNode, sizeChange);
    }

    private void addNodeToTail(Node<T> newNode, Node<T> newTailNode, int sizeChange) {
        if (tail.compareAndSet(null, newTailNode)) {
            head.set(newNode);
        } else {
            Node<T> currentTail;
            do {
                currentTail = tail.get();
            } while (!currentTail.next.compareAndSet(null, newNode));

            tail.set(newTailNode);
        }
        size.addAndGet(sizeChange);
    }



    @Override
    public boolean addAll(Collection<? extends T> col) {
        if (!col.isEmpty()) {
            if (col instanceof ConcurrentQuickAppendList) {
                ConcurrentQuickAppendList<T> cqal = (ConcurrentQuickAppendList<T>) col;
                addNodeToTail(cqal.head.get(), cqal.tail.get(), col.size());
            } else {
                for (T t : col) {
                    add(t);
                }
            }
            return true;
        } else {
            return false;
        }
    }


    @Override
    public T get(int index) {
        return nodeAt(index).elem;
    }

    @Override
    public T set(int index, T val) {
        Node<T> node = nodeAt(index);
        T old = node.elem;
        node.elem = val;
        return old;
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public void clear() {
        while (true) {
            int size = this.size.get();
            tail.set(null);
            head.set(null);
            if (this.size.compareAndSet(size, 0)) {
                return;
            }
        }
    }

    private Node<T> nodeAt(int index) {
        Preconditions.checkElementIndex(index, size());
        Node<T> node = head.get();
        while (--index >= 0) {
            node = node.next.get();
        }
        return node;
    }

    private static class Node<T> {
        private volatile T elem;

        private final AtomicReference<Node<T>> next = new AtomicReference<>();

        private Node(T elem) {
            this.elem = elem;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new AbstractIterator<T>() {

            Node<T> node = head.get();
            Node<T> prev;
            Node<T> prev2;

            @Override
            protected T computeNext() {
                if (node == null) {
                    return endOfData();
                } else {
                    T next = node.elem;
                    prev2 = prev;
                    prev = node;
                    node = node.next.get();
                    return next;
                }
            }
        };
    }
}
