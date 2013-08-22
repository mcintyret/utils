package com.mcintyret.utils.random;

import org.testng.annotations.Test;

import java.util.*;

import static org.testng.AssertJUnit.assertNotNull;

/**
 * User: mcintyret2
 * Date: 12/07/2013
 */

@Test
public class RandomObjectBuilderTest {

    public void shouldGetRandomString() {
        String string = RandomObjectBuilder.randomObject(String.class);

        assertNotNull(string);
    }

    public void shouldGetRandomSimpleObject() {
        SimpleObject so = RandomObjectBuilder.randomObject(SimpleObject.class);
        System.out.println(so);
    }

    public void shouldGetRandomComplexObject() {
        MoreComplexObject mco = RandomObjectBuilder.randomObject(MoreComplexObject.class);

        System.out.println("wow");
    }

    public void shouldGetRandomArray() {
        Double[] doubles = RandomObjectBuilder.randomObject(Double[].class);

        assertNotNull(doubles);
    }

    public void shouldGetRandomMultiDimensionalArray() {
        Double[][][][] doubles = RandomObjectBuilder.randomObject(Double[][][][].class);

        assertNotNull(doubles);
    }

    public void shouldGetRandomPrimitiveArray() {
        int[] ints = RandomObjectBuilder.randomObject(int[].class);

        assertNotNull(ints);
    }

    public void shouldGetRandomMultiDimensionalPrimitiveArray() {
        float[][][][] floats = RandomObjectBuilder.randomObject(float[][][][].class);

        assertNotNull(floats);
    }

    public static class SimpleObject {

        private String string;

        private List<Integer> list;

        @Override
        public String toString() {
            return "SimpleObject{" +
                "string='" + string + '\'' +
                ", list=" + list +
                '}';
        }
    }

    public static class MoreComplexObject {
        Map<List<Set<String>>, Map<Integer, Map<List<Queue<SimpleObject>>, Collection<SimpleObject>>>> nightmare;
    }
}
