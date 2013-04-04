package com.mcintyret.utils.collect;

/**
 * User: mcintyret2
 * Date: 04/04/2013
 */
enum ArrayTrieStrategy {

    ASCII {
        @Override
        public int charToIndex(char c) {
            return c;
        }

        @Override
        public char indexToChar(int i) {
            return (char) i;
        }

        @Override
        public int charsetSize() {
            return 128;
        }

        @Override
        public boolean isCharacterInteresting(char c) {
            return c < 128;
        }
    },
    SINGLE_CASE {
        @Override
        public boolean isCharacterInteresting(char c) {
            return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
        }

        @Override
        public int charToIndex(char c) {
            return c >= 'a' ? c - 'a' : c - 'A';
        }

        @Override
        public char indexToChar(int i) {
            return (char) ('a' + i);
        }

        @Override
        public int charsetSize() {
            return 26;
        }
    },
    SINGLE_CASE_NUMERIC {
        @Override
        public int charToIndex(char c) {
            return c <= '9' ? c - '0' : SINGLE_CASE.charToIndex(c) + 10;
        }

        @Override
        public char indexToChar(int i) {
            return i <= 9 ? (char) ('0' + i) : SINGLE_CASE.indexToChar(i - 10);
        }

        @Override
        public int charsetSize() {
            return 36;
        }

        @Override
        public boolean isCharacterInteresting(char c) {
            return (c >= '0' && c <= '9') || SINGLE_CASE.isCharacterInteresting(c);
        }
    },
    MIXED_CASE {
        @Override
        public boolean isCharacterInteresting(char c) {
            return SINGLE_CASE.isCharacterInteresting(c);
        }

        @Override
        public int charToIndex(char c) {
            return c <= 'Z' ? c - 'A' : c - 'a';
        }

        @Override
        public char indexToChar(int i) {
            return i < 26 ? (char) ('a' + i) : (char) ('a' + i);
        }

        @Override
        public int charsetSize() {
            return 52;
        }
    },
    MIXED_CASE_NUMERIC {
        @Override
        public boolean isCharacterInteresting(char c) {
            return SINGLE_CASE_NUMERIC.isCharacterInteresting(c);
        }

        @Override
        public int charToIndex(char c) {
            return c <= '9' ? c - '0' : 10 + MIXED_CASE.charToIndex(c);
        }

        @Override
        public char indexToChar(int i) {
            return i <= 9 ? (char) ('0' + i) : MIXED_CASE.indexToChar(i - 10);
        }

        @Override
        public int charsetSize() {
            return 62;
        }
    };


    public abstract int charToIndex(char c);

    public abstract char indexToChar(int i);

    public abstract int charsetSize();

    public abstract boolean isCharacterInteresting(char c);

}
