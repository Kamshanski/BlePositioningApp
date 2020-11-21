package edu.tpu.dar.ruban.logic.filters;

import edu.tpu.dar.ruban.utils.Pair;

public class Kalman implements Filter<Pair.DoubleDouble> {
    // As in my diploma work
    final double Q;
    final double R;
    double Z = 0.0, K = 1.0, P;

    public Kalman(double q, double r, double p) {
        Q = q;
        R = r;
        P = p;
    }
    public Kalman(double q, double r) {
        Q = P = q;
        R = r;
    }

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
