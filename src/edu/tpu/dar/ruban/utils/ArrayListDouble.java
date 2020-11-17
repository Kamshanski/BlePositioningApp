package edu.tpu.dar.ruban.utils;

public class ArrayListDouble {

    public static final int SIZE_INCREMENT = 15;
    double[] array;
    int size = 0;

    public ArrayListDouble(int initialCapacity) {
        this.array = new double[initialCapacity];
    }

    public ArrayListDouble() {
        this(15);
    }

    public void add(int d) {
        if (array.length >= size) {
            double[] newArray = new double[array.length + SIZE_INCREMENT];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size] = d;
        size++;
    }

    public double get(int index) {
        return array[index];
    }

    public int size() {
        return size;
    }

    public double getSum() {
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += array[i];
        }
        return sum;
    }

    public void clear() {
        size = 0;
    }
}
