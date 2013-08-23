package com.mcintyret.utils.collect;

import com.google.common.base.Preconditions;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 15/08/2013
 */
public final class QuickAppendList<T> extends AbstractList<T> {

    private int size = 0;

    private Node head;
    private Node tail;

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
    public boolean add(T next) {
        if (head == null) {
            head = tail = new Node(next);
        } else {
            tail.next = new Node(next);
            tail = tail.next;
        }
        size++;
        return true;
    }

    @Override
    public void add(int index, T next) {
        Preconditions.checkElementIndex(index, size() + 1, "");
        Node node = new Node(next);
        if (head == null) {
            head = tail = node;
        } else if (index == 0) {
            node.next = head;
            head = node;
        } else if (index == size) {
            tail.next = node;
            tail = node;
        } else {
            Node prev = nodeAt(index - 1);
            node.next = prev.next;
            prev.next = node;
        }
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends T> coll) {
        if (coll.isEmpty()) {
            return false;
        } else {
            if (coll instanceof QuickAppendList) {
                QuickAppendList<T> other = (QuickAppendList<T>) coll;
                if (head == null) {
                    head = other.head;
                } else {
                    tail.next = other.head;
                }
                tail = other.tail;
                size += other.size;
            } else {
                // Can't just use AbstractList because that uses the addAll(coll, pos) variant which this implementation doesn't support
                for (T t : coll) {
                    add(t);
                }
            }
            return true;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new AbstractIterator<T>() {

            Node node = head;
            Node prev = null;
            Node prev2 = null;

            @Override
            protected T computeNext() {
                if (node == null) {
                    return endOfData();
                } else {
                    T next = node.elem;
                    prev2 = prev;
                    prev = node;
                    node = node.next;
                    return next;
                }
            }

            @Override
            protected void doRemove(T removed) {
                if (prev2 == null) {
                    // we are removing head
                    head = head.next;
                } else {
                    prev2.next.next = prev2.next;
                }
                size--;
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        head = tail = null;
    }

    private Node nodeAt(int index) {
        Preconditions.checkElementIndex(index, size());
        Node node = head;
        while (--index >= 0) {
            node = node.next;
        }
        return node;
    }

    private class Node {
        Node next;
        T elem;

        private Node(T elem) {
            this.elem = elem;
        }
    }

}
