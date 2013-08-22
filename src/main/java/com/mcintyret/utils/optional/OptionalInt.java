package com.mcintyret.utils.optional;

/**
 * User: mcintyret2
 * Date: 22/08/2013
 */
public abstract class OptionalInt {
    
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
}