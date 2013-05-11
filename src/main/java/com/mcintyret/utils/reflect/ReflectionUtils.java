package com.mcintyret.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * User: mcintyret2
 * Date: 25/04/2013
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
        // Cannot be instantiated
    }

    public static Object getField(Object target, String fieldName) {
        try {
            return getAccessibleField(target.getClass(), fieldName).get(target);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> T setField(Object target, String fieldName, T newVal) {
        try {
            Field f = getAccessibleField(target.getClass(), fieldName);
            T oldVal = (T) f.get(target);
            f.set(target, newVal);
            return oldVal;
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static Object invokeMethod(Object target, String methodName, Object... args) {
        Method[] methods = target.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                Class<?>[] paramTypes = m.getParameterTypes();
                if (args.length == paramTypes.length) {
                    boolean match = true;
                    for (int i = 0; i < args.length; i++) {
                        if (!(args[i] == null || args[i].getClass().isAssignableFrom(paramTypes[i]))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        m.setAccessible(true);
                        try {
                            return m.invoke(target, args);
                        } catch (Exception e) {
                            throw new ReflectionException(e);
                        }
                    }
                }
            }
        }
        throw new ReflectionException(new NoSuchMethodError("No method " + methodName + " with parameter types " +
                "satisfiable by " + Arrays.toString(args) + " exists on class " + target.getClass()));
    }

    static Field getAccessibleField(Class<?> clazz, String fieldName) {
        try {
            Field f = clazz.getField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            throw new ReflectionException(e);
        }
    }

}
