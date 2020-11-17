package edu.tpu.dar.ruban.logic.filters;

public interface Filter<T> {
    void put(int rssi);
    T calculate();
}
