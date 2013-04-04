package com.mcintyret.utils.collect;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

/**
 * User: mcintyret2
 * Date: 02/04/2013
 */

@Test
public class FileIterableTest {

    public void shouldIterateFile() {
        Iterable<String> it = new FileIterable("/Users/mcintyret2/Workspace/utils/src/test/resources/test-file");

        assertTrue(Iterables.elementsEqual(Lists.newArrayList("foo", "bar", "baz"), it));
    }

}
