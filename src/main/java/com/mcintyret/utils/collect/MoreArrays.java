package com.mcintyret.utils.collect;

import java.lang.reflect.Array;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: mcintyret2
 * Date: 05/04/2013
 */
public final class MoreArrays {

    private MoreArrays() {

    }

    public static int[] addToStart(int[] array, int... toAdd) {
        return addToStart(array, toAdd, toAdd.length);
    }

    public static int[] addToStart(int[] existing, int[] incoming, int numNew) {
        checkNotNull(existing);
        checkArgument(numNew <= incoming.length);
        if (numNew == 0) {
            return existing;
        } else {
            int[] all = new int[existing.length + numNew];
            System.arraycopy(incoming, 0, all, 0, numNew);
            System.arraycopy(existing, 0, all, numNew, all.length - numNew);
            return all;
        }
    }

    public static long[] addToStart(long[] array, long... toAdd) {
        return addToStart(array, toAdd, toAdd.length);
    }

    public static long[] addToStart(long[] existing, long[] incoming, int numNew) {
        checkNotNull(existing);
        checkArgument(numNew <= incoming.length);
        if (numNew == 0) {
            return existing;
        } else {
            long[] all = new long[existing.length + numNew];
            System.arraycopy(incoming, 0, all, 0, numNew);
            System.arraycopy(existing, 0, all, numNew, all.length - numNew);
            return all;
        }
    }

    public static float[] addToStart(float[] array, float... toAdd) {
        return addToStart(array, toAdd, toAdd.length);
    }

    public static float[] addToStart(float[] existing, float[] incoming, int numNew) {
        checkNotNull(existing);
        checkArgument(numNew <= incoming.length);
        if (numNew == 0) {
            return existing;
        } else {
            float[] all = new float[existing.length + numNew];
            System.arraycopy(incoming, 0, all, 0, numNew);
            System.arraycopy(existing, 0, all, numNew, all.length - numNew);
            return all;
        }
    }

    public static double[] addToStart(double[] array, double... toAdd) {
        return addToStart(array, toAdd, toAdd.length);
    }

    public static double[] addToStart(double[] existing, double[] incoming, int numNew) {
        checkNotNull(existing);
        checkArgument(numNew <= incoming.length);
        if (numNew == 0) {
            return existing;
        } else {
            double[] all = new double[existing.length + numNew];
            System.arraycopy(incoming, 0, all, 0, numNew);
            System.arraycopy(existing, 0, all, numNew, all.length - numNew);
            return all;
        }
    }

    public static char[] addToStart(char[] array, char... toAdd) {
        return addToStart(array, toAdd, toAdd.length);
    }

    public static char[] addToStart(char[] existing, char[] incoming, int numNew) {
        checkNotNull(existing);
        checkArgument(numNew <= incoming.length);
        if (numNew == 0) {
            return existing;
        } else {
            char[] all = new char[existing.length + numNew];
            System.arraycopy(incoming, 0, all, 0, numNew);
            System.arraycopy(existing, 0, all, numNew, all.length - numNew);
            return all;
        }
    }

    public static short[] addToStart(short[] array, short... toAdd) {
        return addToStart(array, toAdd, toAdd.length);
    }

    public static short[] addToStart(short[] existing, short[] incoming, int numNew) {
        checkNotNull(existing);
        checkArgument(numNew <= incoming.length);
        if (numNew == 0) {
            return existing;
        } else {
            short[] all = new short[existing.length + numNew];
            System.arraycopy(incoming, 0, all, 0, numNew);
            System.arraycopy(existing, 0, all, numNew, all.length - numNew);
            return all;
        }
    }

    public static byte[] addToStart(byte[] array, byte... toAdd) {
        return addToStart(array, toAdd, toAdd.length);
    }

    public static byte[] addToStart(byte[] existing, byte[] incoming, int numNew) {
        checkNotNull(existing);
        checkArgument(numNew <= incoming.length);
        if (numNew == 0) {
            return existing;
        } else {
            byte[] all = new byte[existing.length + numNew];
            System.arraycopy(incoming, 0, all, 0, numNew);
            System.arraycopy(existing, 0, all, numNew, all.length - numNew);
            return all;
        }
    }

    public static boolean[] addToStart(boolean[] array, boolean... toAdd) {
        return addToStart(array, toAdd, toAdd.length);
    }

    public static boolean[] addToStart(boolean[] existing, boolean[] incoming, int numNew) {
        checkNotNull(existing);
        checkArgument(numNew <= incoming.length);
        if (numNew == 0) {
            return existing;
        } else {
            boolean[] all = new boolean[existing.length + numNew];
            System.arraycopy(incoming, 0, all, 0, numNew);
            System.arraycopy(existing, 0, all, numNew, all.length - numNew);
            return all;
        }
    }

    public static <T> T[] addToStart(T[] array, T... toAdd) {
        return addToStart(array, toAdd, toAdd.length);
    }

    public static <T> T[] addToStart(T[] existing, T[] incoming, int numNew) {
        checkNotNull(existing);
        checkArgument(numNew <= incoming.length);
        if (numNew == 0) {
            return existing;
        } else {
            T[] all = (T[]) Array.newInstance(existing.getClass().getComponentType(), existing.length + numNew);
            System.arraycopy(incoming, 0, all, 0, numNew);
            System.arraycopy(existing, 0, all, numNew, all.length - numNew);
            return all;
        }
    }

    public static <T> T getLast(T[] array) {
        return array[array.length - 1];
    }

    public boolean getLast(boolean[] array) {
        return array[array.length - 1];
    }

    public char getLast(char[] array) {
        return array[array.length - 1];
    }

    public byte getLast(byte[] array) {
        return array[array.length - 1];
    }

    public short getLast(short[] array) {
        return array[array.length - 1];
    }

    public int getLast(int[] array) {
        return array[array.length - 1];
    }

    public long getLast(long[] array) {
        return array[array.length - 1];
    }

    public float getLast(float[] array) {
        return array[array.length - 1];
    }

    public double getLast(double[] array) {
        return array[array.length - 1];
    }

    public static boolean[] toBooleanArray(Collection<Boolean> c) {
        boolean[] array = new boolean[c.size()];
        int i = 0;
        for (Boolean f : c) {
            array[i++] = f;
        }
        return array;
    }

    public static char[] toCharArray(Collection<Character> c) {
        char[] array = new char[c.size()];
        int i = 0;
        for (Character f : c) {
            array[i++] = f;
        }
        return array;
    }

    public static byte[] toByteArray(Collection<Byte> c) {
        byte[] array = new byte[c.size()];
        int i = 0;
        for (Byte f : c) {
            array[i++] = f;
        }
        return array;
    }

    public static short[] toShortArray(Collection<Short> c) {
        short[] array = new short[c.size()];
        int i = 0;
        for (Short f : c) {
            array[i++] = f;
        }
        return array;
    }

    public static int[] toIntArray(Collection<Integer> c) {
        int[] array = new int[c.size()];
        int i = 0;
        for (Integer f : c) {
            array[i++] = f;
        }
        return array;
    }

    public static long[] toLongArray(Collection<Long> c) {
        long[] array = new long[c.size()];
        int i = 0;
        for (Long f : c) {
            array[i++] = f;
        }
        return array;
    }

    public static float[] toFloatArray(Collection<Float> c) {
        float[] array = new float[c.size()];
        int i = 0;
        for (Float f : c) {
            array[i++] = f;
        }
        return array;
    }

    public static double[] toDoubleArray(Collection<Double> c) {
        double[] array = new double[c.size()];
        int i = 0;
        for (Double f : c) {
            array[i++] = f;
        }
        return array;
    }



}
