package com.mcintyret.utils.optional;

/**
 * User: mcintyret2
 * Date: 22/08/2013
 */
public abstract class OptionalFloat {

    public static OptionalFloat absent() {
        return Absent.INSTANCE;
    }

    public static OptionalFloat of(float f) {
        return new Present(f);
    }

    public abstract boolean isPresent();

    public abstract float get();

    public abstract OptionalFloat or(OptionalFloat secondChoice);

    public abstract float or(float other);

    public abstract float orZero();

    public abstract float orNan();

    private static class Present extends OptionalFloat {

        private final float f;

        private Present(float f) {
            this.f = f;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public float get() {
            return f;
        }

        @Override
        public OptionalFloat or(OptionalFloat secondChoice) {
            return this;
        }

        @Override
        public float or(float other) {
            return f;
        }

        @Override
        public float orZero() {
            return f;
        }

        @Override
        public float orNan() {
            return f;
        }
    }

    private static class Absent extends OptionalFloat {
        private static final OptionalFloat INSTANCE = new Absent();

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public float get() {
            throw new IllegalStateException("OptionalFloat.get() cannot be called on an absent value");
        }

        @Override
        public OptionalFloat or(OptionalFloat secondChoice) {
            return secondChoice;
        }

        @Override
        public float or(float other) {
            return other;
        }

        @Override
        public float orZero() {
            return 0F;
        }

        @Override
        public float orNan() {
            return Float.NaN;
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
        return other instanceof OptionalFloat && get() == ((OptionalFloat) other).get();
    }

    @Override
    public int hashCode() {
        return isPresent() ? 0 : Float.floatToIntBits(get());
    }
}
