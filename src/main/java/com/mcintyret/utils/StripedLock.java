package com.mcintyret.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: mcintyret2
 * Date: 02/01/2013
 */
class StripedLock<T> {

    private final Lock[] locks;
    private static final int DEFAULT_STRIPES = 8;

    public StripedLock(int stripes) {
        locks = new Lock[stripes];
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    public StripedLock() {
        this(DEFAULT_STRIPES);
    }

    // A quirk of two's-compliment integer representations is that Integer.MIN_VALUE == -Integer.MIN_VALUE, so we need
    // to check for that to avoid a negative array index.
    public Lock getLockFor(T o) {
        int hash = o.hashCode();
        return hash == Integer.MIN_VALUE ? locks[0] : locks[Math.abs(o.hashCode()) % locks.length];
    }

    public void lockFor(T o) {
        getLockFor(o).lock();
    }

    public void unlockFor(T o) {
        getLockFor(o).unlock();
    }

    public void lockAll() {
        for (Lock lock : locks) {
            lock.lock();
        }
    }

    public void unlockAll() {
        for (Lock lock : locks) {
            lock.unlock();
        }
    }

}
