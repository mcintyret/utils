package com.mcintyret.utils.serialize;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * User: mcintyret2
 * Date: 11/05/2013
 */
public final class Serializers {

    private static final Map<String, Serializer> SERIALIZERS = Maps.newConcurrentMap();

    private Serializers() {

    }

    static {
        registerSerializer(new HessianSerializer());
        registerSerializer(new JavaSerializer());
        registerSerializer(new JsonSerializer());
    }

    public static void registerSerializer(Serializer serializer) {
        registerSerializer(serializer, false);
    }

    public static void registerSerializer(Serializer serializer, boolean force) {
        if (!force && SERIALIZERS.containsKey(serializer.getSuffix())) {
            throw new IllegalArgumentException("Serializer already registered for suffix '" + serializer.getSuffix() + "'");
        }
        SERIALIZERS.put(serializer.getSuffix(), serializer);
    }

    public static Serializer getSerializer(String filename) {
        Serializer ser = null;
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0) {
            ser = SERIALIZERS.get(filename.substring(lastDot));
        }
        return ser;
    }

    private static Serializer getSerializerOrDie(String filename) {
        Serializer ser = getSerializer(filename);
        if (ser == null) {
            throw new IllegalArgumentException("No registered serializer found for " + filename);
        }
        return ser;
    }

    public static <T> T deserialize(String filename, Class<T> clazz) {
         return getSerializerOrDie(filename).deserialize(filename, clazz);
    }

    public static void serialize(String filename, Object obj) {
        getSerializerOrDie(filename).serialize(obj, filename);
    }

    public static void convert(String oldFilename, Class<?> expectedClass, String newSuffix) {
        if (!oldFilename.endsWith(newSuffix)) {
            Object obj = getSerializerOrDie(oldFilename).deserialize(oldFilename, expectedClass);

            int lastDot = oldFilename.lastIndexOf('.');
            String newFileName = oldFilename.substring(0, lastDot) + newSuffix;
            getSerializerOrDie(newSuffix).serialize(obj, newFileName);
        }
    }

    public static void convert(String oldFilename, String newSuffix) {
        convert(oldFilename, Object.class, newSuffix);
    }
}
