package com.mcintyret.utils.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: mcintyret2
 * Date: 18/08/2013
 */
public class ConcurrentLinkedList<T> extends AbstractList<T> {

    private final AtomicInteger size = new AtomicInteger();

    private final AtomicReference<Node> head = new AtomicReference<>();

    private final AtomicReference<Node> tail = new AtomicReference<>();

    @Override
    public boolean add(T elem) {
        Node node = new Node(elem);
        if (tail.compareAndSet(null, node)) {
            head.set(node);
        } else {
            Node currentTail = tail.get();
            while (true) {
                if (currentTail.next.compareAndSet(null, node)) {
                    break;
                } else {
                    currentTail = currentTail.next.get();
                }
            }
            tail.set(currentTail.next.get());
        }
        size.incrementAndGet();
        return true;
    }


    @Override
    public T get(int index) {
        return nodeAt(index).elem;
    }

    @Override
    public T set(int index, T val) {
        Node node = nodeAt(index);
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

    private Node nodeAt(int index) {
        Preconditions.checkElementIndex(index, size());
        Node node = head.get();
        while (--index >= 0) {
            node = node.next.get();
        }
        return node;
    }

    private class Node {
        private volatile T elem;

        private final AtomicReference<Node> next = new AtomicReference<>();

        private Node(T elem) {
            this.elem = elem;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new AbstractIterator<T>() {

            Node node = head.get();

            @Override
            protected T computeNext() {
                if (node == null) {
                    return endOfData();
                } else {
                    T next = node.elem;
                    node = node.next.get();
                    return next;
                }
            }
        };
    }
}
