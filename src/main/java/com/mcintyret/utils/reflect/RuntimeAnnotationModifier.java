package com.mcintyret.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static com.mcintyret.utils.reflect.ReflectionUtils.*;

/**
 * User: mcintyret2
 * Date: 25/04/2013
 */
public class RuntimeAnnotationModifier {

    private static final Field CLASS_ANNOTATIONS_FIELD = accessible(getField(Class.class, "annotations"));
    private static final Method CLASS_ANNOTATIONS_INIT_METHOD = accessible(getMethod(Class.class, "initAnnotationsIfNecessary"));

    public static <A extends Annotation> A setClassAnnotation(Class<?> clazz, A annotation) {
        return setAnnotation(CLASS_ANNOTATIONS_FIELD, CLASS_ANNOTATIONS_INIT_METHOD, clazz, annotation);
    }

    private static final Field METHOD_ANNOTATIONS_FIELD = accessible(getField(Method.class, "declaredAnnotations"));
    private static final Method METHOD_ANNOTATIONS_INIT_METHOD = accessible(getMethod(Method.class, "declaredAnnotations"));

    public static <A extends Annotation> A setMethodAnnotation(Method method, A annotation) {
        return setAnnotation(METHOD_ANNOTATIONS_FIELD, METHOD_ANNOTATIONS_INIT_METHOD, method, annotation);
    }
    
    private static final Field FIELD_ANNOTATIONS_FIELD = accessible(getField(Field.class, "declaredAnnotations"));
    private static final Method FIELD_ANNOTATIONS_INIT_METHOD = accessible(getMethod(Field.class, "declaredAnnotations"));

    public static <A extends Annotation> A setFieldAnnotation(Field field, A annotation) {
        return setAnnotation(FIELD_ANNOTATIONS_FIELD, FIELD_ANNOTATIONS_INIT_METHOD, field, annotation);
    }

    private static <A extends Annotation> A setAnnotation(Field annotationField, Method annotationInitMethod, Object obj, A annotation) {
        try {
            annotationInitMethod.invoke(obj);
            Map<Class<? extends Annotation>, Annotation> annotationMap = (Map<Class<? extends Annotation>, Annotation>) annotationField.get(obj);
            return (A) annotationMap.put(annotation.getClass(), annotation);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }
}
