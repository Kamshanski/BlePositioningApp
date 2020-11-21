package edu.tpu.dar.ruban.logic.filters;

import edu.tpu.dar.ruban.utils.SlidingWindowIntArray;

import static java.lang.Double.NaN;

public class KaufmansAdaptiveMovingAverage implements Filter<Double> {
    final int size, f, s;
    final double F, S, F_S;

    double ER, alpha, direction, volatility, Z;
    SlidingWindowIntArray window;

    public KaufmansAdaptiveMovingAverage(int initValue) {
        this(10, 2, 30, initValue);
    }

    public KaufmansAdaptiveMovingAverage() {
        this(10, 2, 30, -60);
    }

    public KaufmansAdaptiveMovingAverage(int n, int f, int s, int initValue) {
        this.size = n;
        this.f = f;
        this.s = s;
        F = 2/((double)f+1);
        S = 2/((double)s+1);
        F_S = F-S;
        window = new SlidingWindowIntArray(size, initValue);
    }

    @Override
    public void put(int rssi) {
        window.put(rssi);
        direction = Math.abs(window.get(0) - window.get(-1));
        volatility = getVolatility();
        if (Double.isNaN(ER) || Math.abs(volatility) < 0.01) {
            Z = rssi;
        } else {
            ER = direction / volatility;
            alpha = Math.pow(ER * F_S + S, 2);
            Z = alpha * rssi + (1 - alpha) * Z;
        }
    }

    double getVolatility() {
        double v = 0;
        int rssiPrev = window.get(0);
        int rssiCur = 0;
        for (int i = 1; i < size ; i++) {
            rssiCur = window.get(i);
            v += Math.abs(rssiCur - rssiPrev);
            rssiPrev = rssiCur;
        }
        return v;
    }

    @Override
    public Double calculate() {
        return Z;
    }

}
