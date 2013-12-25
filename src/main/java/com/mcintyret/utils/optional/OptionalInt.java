package com.mcintyret.utils.optional;

/**
 * User: mcintyret2
 * Date: 22/08/2013
 */
public abstract class OptionalInt implements Comparable<OptionalInt> {
    
    public static OptionalInt absent() {
        return Absent.INSTANCE;
    }
    
    public static OptionalInt of(int i) {
        return new Present(i);
    }
    
    public abstract boolean isPresent();
    
    public abstract int get();
    
    public abstract OptionalInt or(OptionalInt secondChoice);
    
    public abstract int or(int other);
    
    public abstract int orZero();

    @Override
    public int compareTo(OptionalInt o) {
        if (isPresent()) {
            return o.isPresent() ? Integer.compare(get(), o.get()) : -1;
        } else {
            return o.isPresent() ? 1 : 0;
        }
    }
    
    private static class Present extends OptionalInt {

        private final int i;

        private Present(int i) {
            this.i = i;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public int get() {
            return i;
        }

        @Override
        public OptionalInt or(OptionalInt secondChoice) {
            return this;
        }

        @Override
        public int or(int other) {
            return i;
        }

        @Override
        public int orZero() {
            return i;
        }
    }
    
    private static class Absent extends OptionalInt {
        private static final OptionalInt INSTANCE = new Absent();

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public int get() {
            throw new IllegalStateException("OptionalInt.get() cannot be called on an absent value");
        }

        @Override
        public OptionalInt or(OptionalInt secondChoice) {
            return secondChoice;
        }

        @Override
        public int or(int other) {
            return other;
        }

        @Override
        public int orZero() {
            return 0;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        // if other != this, it must be present
        return other instanceof OptionalInt && get() == ((OptionalInt) other).get();
    }

    @Override
    public int hashCode() {
        return isPresent() ? get() : 0;
    }
}