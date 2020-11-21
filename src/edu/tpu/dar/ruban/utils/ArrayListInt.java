package edu.tpu.dar.ruban.utils;

import java.util.Arrays;

public class ArrayListInt {
    public final int sizeIncrement;
    int[] array;
    int size = 0;

    public ArrayListInt(int initialCapacity, int sizeIncrement) {
        this.sizeIncrement = sizeIncrement;
        this.array = new int[initialCapacity];
    }

    public ArrayListInt(int initialCapacity) {
        this(initialCapacity, 15);
    }

    public ArrayListInt() {
        this(15);
    }

    public void add(int d) {
        if (array.length <= size) {
            int[] newArray = new int[array.length + sizeIncrement];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size] = d;
        size++;
    }

    public int get(int index) {
        return array[index];
    }

    public int size() {
        return size;
    }

    public int getSum() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += array[i];
        }
        return sum;
    }

    public void clear() {
        size = 0;
    }

    public int[] toArray() {
        return Arrays.copyOf(array, size);
    }

}
