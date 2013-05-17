package com.mcintyret.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import static com.mcintyret.utils.reflect.ReflectionUtils.*;

/**
 * User: mcintyret2
 * Date: 25/04/2013
 */
public class RuntimeAnnotationModifier {

    private static final Field CLASS_ANNOTATIONS_FIELD = accessible(getField(Class.class, "annotations"));

    public static <A extends Annotation> A setClassAnnotation(Class<?> clazz, A annotation) {
        try {
            Map<Class<? extends Annotation>, Annotation> annotationMap = (Map<Class<? extends Annotation>, Annotation>) CLASS_ANNOTATIONS_FIELD.get(clazz);
            return (A) annotationMap.put(annotation.getClass(), annotation);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }
}
