package com.mcintyret.utils.collect;

import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * User: mcintyret2
 * Date: 14/05/2013
 */

@Test
public class SortedListTest {

    public void shouldAddSorted() {
        List<Integer> list = new SortedList<>();

        list.add(4);
        list.add(8);
        list.add(6);
        list.add(45);
        list.add(2);

        assertEquals(Lists.newArrayList(2, 4, 6, 8, 45), list);
    }

    public void shouldAddAllSorted() {
        List<Integer> list = new SortedList<>();
        list.addAll(Lists.newArrayList(5, 8, 1, 5, 2, 3, 9));

        assertEquals(Lists.newArrayList(1, 2, 3, 5, 5, 8, 9), list);
    }
}
