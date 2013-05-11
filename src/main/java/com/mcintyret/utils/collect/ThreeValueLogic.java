package com.mcintyret.utils.collect;

/**
 * User: mcintyret2
 * Date: 05/04/2013
 */
public enum ThreeValueLogic {

    TRUE {
        @Override
        public boolean asBoolean() {
            return true;
        }

        @Override
        public ThreeValueLogic and(ThreeValueLogic other) {
            return other == UNKNOWN ? UNKNOWN : other;
        }

        @Override
        public ThreeValueLogic or(ThreeValueLogic other) {
            return this;
        }

        @Override
        public ThreeValueLogic negate() {
            return FALSE;
        }
    },
    FALSE {
        @Override
        public ThreeValueLogic and(ThreeValueLogic other) {
            return this;
        }

        @Override
        public ThreeValueLogic or(ThreeValueLogic other) {
            return other;
        }

        @Override
        public ThreeValueLogic negate() {
            return TRUE;
        }

        @Override
        public boolean asBoolean() {
            return false;
        }
    },
    UNKNOWN {
        @Override
        public ThreeValueLogic and(ThreeValueLogic other) {
            return this;
        }

        @Override
        public ThreeValueLogic or(ThreeValueLogic other) {
            return other == TRUE ? TRUE : this;
        }

        @Override
        public ThreeValueLogic negate() {
            return this;
        }

        @Override
        public boolean asBoolean() {
            throw new UnknownStateToBooleanException();
        }
    };

    public abstract ThreeValueLogic and(ThreeValueLogic other);

    public abstract ThreeValueLogic or(ThreeValueLogic other);

    public abstract ThreeValueLogic negate();

    public abstract boolean asBoolean();

    public ThreeValueLogic and(boolean other) {
        return and(valueOf(other));
    }

    public ThreeValueLogic or(boolean other) {
        return or(valueOf(other));
    }

    public static ThreeValueLogic valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    public static class UnknownStateToBooleanException extends RuntimeException {

    }
}
