package com.mcintyret.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * User: mcintyret2
 * Date: 25/04/2013
 */
public class RuntimeAnnotationModifier {

    private static final Field field = getField();

    private static Field getField() {
        try {
            Field field = Class.class.getField("annotations");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setClassAnnotation(Class<?> clazz, Annotation annotation) {
        try {
        Map<Class<? extends Annotation>, Annotation> annotationMap = field.get(clazz);
        } catch ()
    }

}
