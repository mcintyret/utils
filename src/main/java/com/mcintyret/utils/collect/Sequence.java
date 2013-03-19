package com.mcintyret.utils.collect;

import java.util.List;

/**
 * User: mcintyret2
 * Date: 18/03/2013
 */
public interface Sequence<T> {

    T computeNext(List<T> previous);

}
