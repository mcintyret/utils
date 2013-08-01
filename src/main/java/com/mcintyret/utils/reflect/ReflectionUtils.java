package com.mcintyret.utils.reflect;

import com.mcintyret.utils.collect.MoreIterables;

import java.lang.reflect.*;
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
            return accessible(getField(target.getClass(), fieldName)).get(target);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> T setField(Object target, String fieldName, T newVal) {
        try {
            Field f = accessible(getField(target.getClass(), fieldName));
            T oldVal = (T) f.get(target);
            f.set(target, newVal);
            return oldVal;
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }

    public static Object invokeMethod(Object target, String methodName, Object... args) {
        try {
            return getMethod(target.getClass(), methodName, args).invoke(target, args);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }


    static Method getMethod(Class<?> targetClass, String methodName, Object... args) {
        for (Method method : MoreIterables.concat(targetClass.getMethods(), targetClass.getDeclaredMethods())) {
            if (method.getName().equals(methodName)) {
                if (argumentsApplicable(args, method.getParameterTypes())) {
                    try {
                        return method;
                    } catch (Exception e) {
                        throw new ReflectionException(e);
                    }
                }
            }
        }
        throw new ReflectionException(new NoSuchMethodError("No method " + methodName + " with parameter types " +
                    "satisfiable by " + Arrays.toString(args) + " exists on class " + targetClass));
    }

    public static <T> T newInstance(Class<T> clazz, Object... constructorArgs) {
        // try a shortcut
        if (constructorArgs.length == 0) {
            try {
                return clazz.newInstance();
            } catch (ReflectiveOperationException e) {
                // ignore
            }
        }

        try {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (argumentsApplicable(constructorArgs, constructor.getParameterTypes())) {
                    return (T) accessible(constructor).newInstance(constructorArgs);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }

        throw new ReflectionException(new NoSuchMethodError("No constructor with parameter types " +
            "satisfiable by " + Arrays.toString(constructorArgs) + " exists on class " + clazz));
    }

    static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // ignore
        }

        try {
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            // ignore
        }
        throw new ReflectionException(new NoSuchFieldException("No field named " + fieldName + " found in class " + clazz));

    }

    private static boolean argumentsApplicable(Object[] args, Class<?>[] parameterTypes) {
        if (args.length != parameterTypes.length) {
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            if (!args[i].getClass().isAssignableFrom(parameterTypes[i])) {
                return false;
            }
        }
        return true;
    }

    public static Class<?>[] toClassArray(Object... args) {
        Class<?>[] classes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            classes[i] = args[i].getClass();
        }
        return classes;
    }

    public static <A extends AccessibleObject> A accessible(A accessible) {
        accessible.setAccessible(true);
        return accessible;
    }

}
