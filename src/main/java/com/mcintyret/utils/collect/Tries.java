package com.mcintyret.utils.collect;

import com.google.common.base.Function;
import com.google.common.base.Functions;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * User: mcintyret2
 * Date: 21/03/2013
 */
public final class Tries {

    private static final Function<Object, String> DEFAULT_FUNCTION = Functions.toStringFunction();

    private static <T> Function<T, String> defaultFunction() {
        return (Function<T, String>) DEFAULT_FUNCTION;
    }

    private Tries() {

    }

    public static <T> Trie<T> emptyTrie() {
        return (Trie<T>) EmptyTrie.INSTANCE;
    }

    public static <T> Trie<T> newHashTrie() {
        return newHashTrie(Tries.<T>defaultFunction());
    }

    public static <T> Trie<T> newHashTrie(Function<T, String> keyToValueFunction) {
        return new HashTrie<>(keyToValueFunction);
    }

    public static <T> Trie<T> newConcurrentHashTrie() {
        return newConcurrentHashTrie(Tries.<T>defaultFunction());
    }

    public static <T> Trie<T> newConcurrentHashTrie(Function<T, String> keyToValueFunction) {
        return new ConcurrentHashTrie<>(keyToValueFunction);
    }

    public static <T> Trie<T> newAsciiArrayTrie() {
        return newAsciiArrayTrie(Tries.<T>defaultFunction());
    }

    public static <T> Trie<T> newAsciiArrayTrie(Function<T, String> keyToValueFunction) {
        return new ArrayTrie<T>(keyToValueFunction) {
            @Override
            public boolean isCharacterInteresting(char c) {
                return c < 128;
            }

            @Override
            protected int charToIndex(char c) {
                return c;
            }

            @Override
            protected int charsetSize() {
                return 128;
            }
        };
    }

    public static <T> Trie<T> newSingleCaseArrayTrie() {
        return newSingleCaseArrayTrie(Tries.<T>defaultFunction());
    }

    public static <T> Trie<T> newSingleCaseArrayTrie(Function<T, String> keyToValueFunction) {
        return new ArrayTrie<T>(keyToValueFunction) {
            @Override
            protected boolean isCharacterInteresting(char c) {
                return isAsciiLetter(c);
            }

            @Override
            protected int charToIndex(char c) {
                return c >= 'a' ? c - 'a' : c - 'A';
            }

            @Override
            protected int charsetSize() {
                return 26;
            }
        };
    }

    public static <T> Trie<T> newSingleCaseNumericArrayTrie() {
        return newSingleCaseNumericArrayTrie(Tries.<T>defaultFunction());
    }

    public static <T> Trie<T> newSingleCaseNumericArrayTrie(Function<T, String> keyToValueFunction) {
        return new ArrayTrie<T>(keyToValueFunction) {
            @Override
            protected boolean isCharacterInteresting(char c) {
                return isAsciiLetterOrNumber(c);
            }

            @Override
            protected int charToIndex(char c) {
                return c >= 'a' ? c - 'a' : (c >= 'A' ? c - 'A' : 26 + c - '0');
            }

            @Override
            protected int charsetSize() {
                return 36;
            }
        };
    }

    public static <T> Trie<T> newMixedCaseArrayTrie() {
        return newMixedCaseArrayTrie(Tries.<T>defaultFunction());
    }

    public static <T> Trie<T> newMixedCaseArrayTrie(Function<T, String> keyToValueFunction) {
        return new ArrayTrie<T>(keyToValueFunction) {
            @Override
            protected boolean isCharacterInteresting(char c) {
                return isAsciiLetter(c);
            }

            @Override
            protected int charToIndex(char c) {
                return c >= 'a' ? c - 'a' : 26 + c - 'A';
            }

            @Override
            protected int charsetSize() {
                return 52;
            }
        };
    }

    public static <T> Trie<T> newMixedCaseNumericArrayTrie() {
        return newMixedCaseNumericArrayTrie(Tries.<T>defaultFunction());
    }

    public static <T> Trie<T> newMixedCaseNumericArrayTrie(Function<T, String> keyToValueFunction) {
        return new ArrayTrie<T>(keyToValueFunction) {
            @Override
            protected boolean isCharacterInteresting(char c) {
                return isAsciiLetterOrNumber(c);
            }

            @Override
            protected int charToIndex(char c) {
                return c >= 'a' ? c - 'a' : (c >= 'A' ? 26 + c - 'A' : 52 + c - '0');
            }

            @Override
            protected int charsetSize() {
                return 62;
            }

        };
    }

    private static boolean isAsciiLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private static boolean isAsciiLetterOrNumber(char c) {
        return isAsciiLetter(c) || (c >= '0' && c <= '9');
    }

    public static <T> Trie<T> unmodifiableTrie(final Trie<T> trie) {
        return new ForwardingTrie<T>() {
            @Override
            protected Trie<T> delegate() {
                return trie;
            }

            @Override
            public void add(T val) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Trie<T> subTrie(String key) {
                return unmodifiableTrie(trie.subTrie(key));
            }

            @Override
            public T put(String key, T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public T remove(Object key) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void putAll(Map<? extends String, ? extends T> m) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void clear() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static class EmptyTrie implements Trie<Object> {
        private static final Trie<Object> INSTANCE = new EmptyTrie();

        @Override
        public void add(Object val) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Trie<Object> subTrie(String key) {
            return this;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Object get(Object key) {
            return null;
        }

        @Override
        public Object put(String key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> m) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
        }

        @Override
        public Set<String> keySet() {
            return Collections.emptySet();
        }

        @Override
        public Collection<Object> values() {
            return Collections.emptySet();
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return Collections.emptySet();
        }
    }
}
