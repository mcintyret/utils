package com.mcintyret.utils.optional;

/**
 * User: mcintyret2
 * Date: 22/08/2013
 */
public abstract class OptionalDouble implements Comparable<OptionalDouble> {
    
    public static OptionalDouble absent() {
        return Absent.INSTANCE;
    }
    
    public static OptionalDouble of(double d) {
        return new Present(d);
    }
    
    public abstract boolean isPresent();
    
    public abstract double get();
    
    public abstract OptionalDouble or(OptionalDouble secondChoice);
    
    public abstract double or(double other);
    
    public abstract double orZero();
    
    public abstract double orNan();

    @Override
    public int compareTo(OptionalDouble o) {
        if (isPresent()) {
            return o.isPresent() ? Double.compare(get(), o.get()) : -1;
        } else {
            return o.isPresent() ? 1 : 0;
        }
    }
    
    private static class Present extends OptionalDouble {

        private final double d;

        private Present(double d) {
            this.d = d;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public double get() {
            return d;
        }

        @Override
        public OptionalDouble or(OptionalDouble secondChoice) {
            return this;
        }

        @Override
        public double or(double other) {
            return d;
        }

        @Override
        public double orZero() {
            return d;
        }

        @Override
        public double orNan() {
            return d;
        }
    }
    
    private static class Absent extends OptionalDouble {
        private static final OptionalDouble INSTANCE = new Absent();

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public double get() {
            throw new IllegalStateException("OptionalDouble.get() cannot be called on an absent value");
        }

        @Override
        public OptionalDouble or(OptionalDouble secondChoice) {
            return secondChoice;
        }

        @Override
        public double or(double other) {
            return other;
        }

        @Override
        public double orZero() {
            return 0D;
        }

        @Override
        public double orNan() {
            return Double.NaN;
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
        return other instanceof OptionalDouble && get() == ((OptionalDouble) other).get();
    }

    @Override
    public int hashCode() {
        return isPresent() ? hashCode(get()) : 0;
    }

    private static int hashCode(double d) {
        long bits = Double.doubleToLongBits(d);
        return (int)(bits ^ (bits >>> 32));
    }
}
