package edu.tpu.dar.ruban.logic.filters;

import edu.tpu.dar.ruban.utils.Pair;

public class Kalman implements Filter<Pair.DoubleDouble> {
    // As in my diploma work
    final double Q = 1.0;
    final double R = 1.0;
    double Z = 0.0, K = 1.0, P = Q;

    @Override
    public void put(int rssi) {
        P = (1-K)*P + Q;
        K = P/(P+R);
        Z = K*rssi + (1-K)*Z;
    }

    @Override
    public Pair.DoubleDouble calculate() {
        // Return filtered result and covariance
        return new Pair.DoubleDouble(Z, P);
    }
}
