package edu.tpu.dar.ruban.utils;

import java.util.Arrays;

// head is at last input
public class SlidingWindowIntArray implements Cloneable {
    int[] window;
    final int size;
    int index = 0;

    public SlidingWindowIntArray(int size, int initValue) {
        this(size);
        Arrays.fill(window, initValue);
    }

    public SlidingWindowIntArray(int size) {
        window = new int[size];
        this.size = size;
    }

    public void put(int datum) {
        window[index] = datum;
        ++index;
        // Mode start index
        if (index >= size) {
            index -= size;
        }
    }

    public int get(int i) {
        int ind = index + i;
        if (ind < 0) {
            while (ind < 0) {
                ind += size;
            }
        } else if (ind >= size) {
            while (ind >= size) {
                ind -= size;
            }
        }
        return window[ind];
    }

    public int[] toArray() {
        return Arrays.copyOf(window, window.length);
    }

    public int getSize() {
        return size;
    }
}
