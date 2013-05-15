package com.mcintyret.utils.collect;

import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.util.List;

/**
 * User: mcintyret2
 * Date: 14/05/2013
 */

@Test
public class SortedListTest {

    public void shouldAddInSortedOrder() {
        List<Integer> list = new SortedList<>();

        list.add(4);
        list.add(8);
        list.add(6);
        list.add(45);
        list.add(2);

        System.out.println(Lists.reverse(list));

    }
}
