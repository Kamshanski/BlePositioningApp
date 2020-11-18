package edu.tpu.dar.ruban.logic.filters;

import edu.tpu.dar.ruban.utils.SlidingWindowIntArray;

import java.util.Arrays;

public class Median implements Filter<Double> {
    SlidingWindowIntArray windowIntArray;

    public Median(int windowSize) {
        windowIntArray = new SlidingWindowIntArray(windowSize);
    }

    @Override
    public void put(int rssi) {
        windowIntArray.put(rssi);
    }

    @Override
    public Double calculate() {
        int[] array = windowIntArray.toArray();
        return median(array);
    }

    public static double median(int[] array) {
        Arrays.sort(array);
        int L_2 = array.length / 2;
        if ((array.length & 1) == 0) {  // even len
            return (array[L_2] + array[L_2 - 1]) / 2.0;
        } else {                        // odd len
            return array[L_2];
        }
    }

}
