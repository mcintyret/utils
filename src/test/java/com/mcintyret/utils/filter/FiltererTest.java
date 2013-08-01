package com.mcintyret.utils.filter;

import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * User: mcintyret2
 * Date: 01/08/2013
 */

@Test
public class FiltererTest {

    public void shouldFilter() {
        ComparableFilter<Integer> one = new ComparableFilter<>(5, true, 8, false);
        Filter<Integer> two = new AbstractFilter<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Filterer<Integer> filterer = new Filterer<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), one.negate(), two);

        List<Integer> result = Lists.newArrayList(filterer.filter());

        assertEquals(Lists.newArrayList(2, 4, 8, 10), result);

        // now we change one of the filters
        one.setMaxInclusive(true);

        result = Lists.newArrayList(filterer.filter());

        assertEquals(Lists.newArrayList(2, 4, 10), result);
    }

}
