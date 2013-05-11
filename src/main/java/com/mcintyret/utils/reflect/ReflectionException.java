package com.mcintyret.utils.reflect;

/**
 * User: mcintyret2
 * Date: 25/04/2013
 *
 * A handy runtime exception to wrap the endless checked reflection exceptions
 */
public class ReflectionException extends RuntimeException {

    public ReflectionException(Throwable cause) {
        super(cause);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(String message) {
        super(message);
    }

}
