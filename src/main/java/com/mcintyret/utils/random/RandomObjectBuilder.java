package com.mcintyret.utils.random;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.lang3.RandomStringUtils;
import com.google.common.collect.Maps;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: mcintyret2
 * Date: 11/07/2013
 */
public final class RandomObjectBuilder {

    private static final Random RNG = new Random();

    private static final long FURTHEST_FUTURE_DATE_MILLIS = ZonedDateTime.now().plusYears(1000).toInstant().toEpochMilli();

    private static final Map<Class<?>, RandomObjectProvider<?>> DEFAULT_PROVIDERS = Maps.newHashMap();
    private static final int COLLECTION_SIZE = 20;

    private static <T> void registerDefaultProvider(Class<T> clazz, RandomObjectProvider<? extends T> provider) {
        DEFAULT_PROVIDERS.put(clazz, provider);
    }

    private static final RandomObjectProvider<String> STRING_RANDOM = new RandomObjectProvider<String>() {
        @Override
        public String random() {
            return RandomStringUtils.random(RNG.nextInt(15));
        }
    };

    private static final RandomObjectProvider<Long> LONG_RANDOM = new RandomObjectProvider<Long>() {
        @Override
        public Long random() {
            return RNG.nextLong();
        }
    };

    private static final RandomObjectProvider<Integer> INTEGER_RANDOM = new RandomObjectProvider<Integer>() {
        @Override
        public Integer random() {
            return RNG.nextInt();
        }
    };

    private static final RandomObjectProvider<Float> FLOAT_RANDOM = new RandomObjectProvider<Float>() {
        @Override
        public Float random() {
            return RNG.nextFloat();
        }
    };

    private static final RandomObjectProvider<Double> DOUBLE_RANDOM = new RandomObjectProvider<Double>() {
        @Override
        public Double random() {
            return RNG.nextDouble();
        }
    };

    private static final RandomObjectProvider<Boolean> BOOLEAN_RANDOM = new RandomObjectProvider<Boolean>() {
        @Override
        public Boolean random() {
            return RNG.nextBoolean();
        }
    };

    private static final RandomObjectProvider<Byte> BYTE_RANDOM = new RandomObjectProvider<Byte>() {
        @Override
        public Byte random() {
            return (byte) RNG.nextInt();
        }
    };

    private static final RandomObjectProvider<Short> SHORT_RANDOM = new RandomObjectProvider<Short>() {
        @Override
        public Short random() {
            return (short) RNG.nextInt();
        }
    };

    private static final RandomObjectProvider<Character> CHARACTER_RANDOM = new RandomObjectProvider<Character>() {
        @Override
        public Character random() {
            return (char) RNG.nextInt();
        }
    };

    private static final RandomObjectProvider<Date> DATE_RANDOM = new RandomObjectProvider<Date>() {
        @Override
        public Date random() {
            return new Date((long) (RNG.nextDouble() * FURTHEST_FUTURE_DATE_MILLIS));
        }
    };

    static {
        registerDefaultProvider(String.class, STRING_RANDOM);
        registerDefaultProvider(Long.class, LONG_RANDOM);
        registerDefaultProvider(long.class, LONG_RANDOM);
        registerDefaultProvider(Integer.class, INTEGER_RANDOM);
        registerDefaultProvider(int.class, INTEGER_RANDOM);
        registerDefaultProvider(Short.class, SHORT_RANDOM);
        registerDefaultProvider(short.class, SHORT_RANDOM);
        registerDefaultProvider(Byte.class, BYTE_RANDOM);
        registerDefaultProvider(byte.class, BYTE_RANDOM);
        registerDefaultProvider(Character.class, CHARACTER_RANDOM);
        registerDefaultProvider(char.class, CHARACTER_RANDOM);
        registerDefaultProvider(Boolean.class, BOOLEAN_RANDOM);
        registerDefaultProvider(boolean.class, BOOLEAN_RANDOM);
        registerDefaultProvider(Float.class, FLOAT_RANDOM);
        registerDefaultProvider(float.class, FLOAT_RANDOM);
        registerDefaultProvider(Double.class, DOUBLE_RANDOM);
        registerDefaultProvider(double.class, DOUBLE_RANDOM);
        registerDefaultProvider(Date.class, DATE_RANDOM);
        registerDefaultProvider(Object.class, STRING_RANDOM);
    }

    private static <T> T safeNewInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T randomObject(Class<T> clazz, Type... generics) {
        return getRandomObjectProvider(clazz, generics).random();
    }

    private static <T> T randomArray(Class<T> arrayClass) {
        Class<?> componentClazz = arrayClass.getComponentType();
        if (componentClazz.isPrimitive()) {
            if (boolean.class == componentClazz) {
                return (T) randomBooleanArray();
            } else if (int.class == componentClazz) {
                return (T) randomIntArray();
            } else if (long.class == componentClazz) {
                return (T) randomLongArray();
            } else if (float.class == componentClazz) {
                return (T) randomFloatArray();
            } else if (double.class == componentClazz) {
                return (T) randomDoubleArray();
            } else if (char.class == componentClazz) {
                return (T) randomCharArray();
            } else if (short.class == componentClazz) {
                return (T) randomShortArray();
            } else {
                return (T) randomByteArray();
            }
        }
        int size = RNG.nextInt(COLLECTION_SIZE);
        Object[] array = (Object[]) Array.newInstance(componentClazz, size);
        RandomObjectProvider<?> provider = getRandomObjectProvider(componentClazz, new Type[0]);
        for (int i = 0; i < array.length; i++) {
            array[i] = provider.random();
        }
        return (T) array;
    }

    private static <K, V, M extends Map<K, V>> M randomMap(Class<M> mapClass, Type keyType, Type valueType) {
        Map<K, V> m = safeNewInstance(mapClass);
        if (m == null) {
            if (EnumMap.class.isAssignableFrom(mapClass)) {
                m = new EnumMap((Class<Enum<?>>) keyType);
            } else if (NavigableMap.class.isAssignableFrom(mapClass)) {
                m = new TreeMap<>();
            } else if (SortedMap.class.isAssignableFrom(mapClass)) {
                m = new TreeMap<>();
            } else if (ConcurrentMap.class.isAssignableFrom(mapClass)) {
                m = new ConcurrentHashMap<>();
            } else {
                m = new HashMap<>();
            }
        }

        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<K> keyProvider = (RandomObjectProvider<K>) getRandomObjectProvider(keyType);
        RandomObjectProvider<V> valueProvider = (RandomObjectProvider<V>) getRandomObjectProvider(valueType);

        for (int i = 0; i < size; i++) {
            m.put(keyProvider.random(), valueProvider.random());
        }
        return (M) m;
    }

    private static <T, C extends Collection<T>> C randomCollection(Class<C> collClass, Type compType) {
        Collection<T> c = safeNewInstance(collClass);
        if (c == null) {
            if (List.class.isAssignableFrom(collClass)) {
                c = new ArrayList<>();
            } else if (Queue.class.isAssignableFrom(collClass)) {
                c = new LinkedList<>();
            } else if (Stack.class.isAssignableFrom(collClass)) {
                c = new Stack<>();
            } else if (Set.class.isAssignableFrom(collClass)) {
                c = new HashSet<>();
            } else if (BlockingQueue.class.isAssignableFrom(collClass)) {
                c = new LinkedBlockingQueue<>();
            } else if (Deque.class.isAssignableFrom(collClass)) {
                c = new ArrayDeque<>();
            } else if (BlockingDeque.class.isAssignableFrom(collClass)) {
                c = new LinkedBlockingDeque<>();
            } else {
                c = new ArrayList<>();
            }
        }
        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<T> provider = (RandomObjectProvider<T>) getRandomObjectProvider(compType);
        for (int i = 0; i < size; i++) {
            c.add(provider.random());
        }
        return (C) c;
    }

    private static RandomObjectProvider<?> getRandomObjectProvider(Type type) {
        if (type instanceof Class<?>) {
            return getRandomObjectProvider((Class<?>) type, new Type[0]);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            return getRandomObjectProvider((Class<?>) pt.getRawType(), pt.getActualTypeArguments());
        } else {
            throw new IllegalArgumentException("No support for " + type.getClass());
        }
    }

    private static <T> RandomObjectProvider<T> loadRandomObjectProvider(Class<T> clazz) {
        return (RandomObjectProvider<T>) DEFAULT_PROVIDERS.get(clazz);
    }

    private static <T> RandomObjectProvider<T> getRandomObjectProvider(Class<T> clazz, Type... generics) {
        RandomObjectProvider<T> provider;
        if (clazz.isEnum()) {
            provider = (RandomObjectProvider<T>) new RandomEnumProvider<>((Class<Enum<?>>) clazz);
        } else if (clazz.isArray()) {
            provider = new RandomArrayProvider<>(clazz);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Type genericType = generics.length >= 1 ? generics[0] : Object.class;
            provider = (RandomObjectProvider<T>) new RandomCollectionProvider<>((Class<? extends Collection>) clazz, genericType);
        } else if (Map.class.isAssignableFrom(clazz)) {
            Type keyType = generics.length >= 1 ? generics[0] : Object.class;
            Type valueType = generics.length >= 2 ? generics[1] : Object.class;
            provider = (RandomObjectProvider<T>) new RandomMapProvider<>((Class<? extends Map>) clazz, keyType, valueType);
        } else {
            provider = loadRandomObjectProvider(clazz);
            if (provider == null) {
                provider = new UnknownRandomObjectProvider<>(clazz);
                registerDefaultProvider(clazz, provider);
            }
        }
        return provider;
    }

    private static class RandomCollectionProvider<T, C extends Collection<T>> implements RandomObjectProvider<C> {

        private final Class<C> collectionClass;

        private final Type componentType;

        private RandomCollectionProvider(Class<C> collectionClass, Type componentType) {
            this.collectionClass = collectionClass;
            this.componentType = componentType;
        }

        @Override
        public C random() {
            return randomCollection(collectionClass, componentType);
        }
    }

    private static class UnknownRandomObjectProvider<T> implements RandomObjectProvider<T> {

        private final Map<Field, RandomObjectProvider<?>> fieldProviders = new HashMap<>();

        private final Class<T> clazz;

        private UnknownRandomObjectProvider(Class<T> clazz) {
            this.clazz = clazz;
            for (Field f : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    f.setAccessible(true);
                    fieldProviders.put(f, getRandomObjectProvider(f.getGenericType()));
                }
            }
        }

        @Override
        public T random() {
            T t = safeNewInstance(clazz);
            checkNotNull(t);
            for (Map.Entry<Field, RandomObjectProvider<?>> entry : fieldProviders.entrySet()) {
                try {
                    entry.getKey().set(t, entry.getValue().random());
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
            return t;
        }
    }


    private final Map<Class<?>, RandomObjectProvider<?>> providers = Maps.newHashMap(DEFAULT_PROVIDERS);

    public RandomObjectBuilder() {

    }

    private static class RandomEnumProvider<E extends Enum<?>> implements RandomObjectProvider<E> {

        private final Class<E> enumClass;

        private RandomEnumProvider(Class<E> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public E random() {
            return RandomUtils.randomElement(enumClass.getEnumConstants());
        }
    }

    private static class RandomArrayProvider<T> implements RandomObjectProvider<T> {

        private final Class<T> componentClass;

        private RandomArrayProvider(Class<T> componentClass) {
            this.componentClass = componentClass;
        }

        @Override
        public T random() {
            return randomArray(componentClass);
        }
    }

    private static boolean[] randomBooleanArray() {
        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<Boolean> provider = loadRandomObjectProvider(boolean.class);
        boolean[] array = new boolean[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = provider.random();
        }
        return array;
    }

    private static byte[] randomByteArray() {
        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<Byte> provider = loadRandomObjectProvider(byte.class);
        byte[] array = new byte[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = provider.random();
        }
        return array;
    }

    private static short[] randomShortArray() {
        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<Short> provider = loadRandomObjectProvider(short.class);
        short[] array = new short[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = provider.random();
        }
        return array;
    }

    private static int[] randomIntArray() {
        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<Integer> provider = loadRandomObjectProvider(int.class);
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = provider.random();
        }
        return array;
    }

    private static long[] randomLongArray() {
        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<Long> provider = loadRandomObjectProvider(long.class);
        long[] array = new long[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = provider.random();
        }
        return array;
    }

    private static float[] randomFloatArray() {
        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<Float> provider = loadRandomObjectProvider(float.class);
        float[] array = new float[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = provider.random();
        }
        return array;
    }

    private static double[] randomDoubleArray() {
        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<Double> provider = loadRandomObjectProvider(double.class);
        double[] array = new double[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = provider.random();
        }
        return array;
    }

    private static char[] randomCharArray() {
        int size = RNG.nextInt(COLLECTION_SIZE);
        RandomObjectProvider<Character> provider = loadRandomObjectProvider(char.class);
        char[] array = new char[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = provider.random();
        }
        return array;
    }

    private static class RandomMapProvider<K, V, M extends Map<K, V>> implements RandomObjectProvider<M> {

        private final Class<M> mapClass;

        private final Type keyType;

        private final Type valueType;

        private RandomMapProvider(Class<M> mapClass, Type keyType, Type valueType) {
            this.mapClass = mapClass;
            this.keyType = keyType;
            this.valueType = valueType;
        }

        @Override
        public M random() {
            return randomMap(mapClass, keyType, valueType);
        }
    }
}
