package com.mcintyret.utils;


/**
 * User: mcintyret2
 * Date: 02/04/2013
 */
public interface ProcessorFactory<T> {

    Processor<T> newProcessor(T t, ThreadedForEach<T> tfe);

}
