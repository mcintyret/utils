package com.mcintyret.utils.io;

import java.io.IOException;

/**
 * User: mcintyret2
 * Date: 10/05/2013
 */
public class RuntimeIoException extends RuntimeException {

    public RuntimeIoException(IOException cause) {
        super(cause);
    }

}
