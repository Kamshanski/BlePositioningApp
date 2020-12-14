package edu.tpu.dar.ruban.utils;

public class ArrayListDouble {

    public final int sizeInclement;
    double[] array;
    int size = 0;

    public ArrayListDouble(int initialCapacity, int sizeIncrement) {
        this.array = new double[initialCapacity];
        this.sizeInclement = sizeIncrement;
    }

    public ArrayListDouble() {
        this(15, 15);
    }

    public void add(double d) {
        if (array.length <= size) {
            double[] newArray = new double[array.length + sizeInclement];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size] = d;
        size++;
    }

    public void add(int[] d) {
        for (double t : d) {
            add(t);
        }
    }

    public double get(int index) {
        if (index < 0) {
            index += size;
        } else if (index >= size){
            index -= size;
        }
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
