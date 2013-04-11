package com.mcintyret.utils.collect;

import java.lang.reflect.Array;

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
    
   
    
}
