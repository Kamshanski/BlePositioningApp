package edu.tpu.dar.ruban.logic.distancefunctions;

public class Atanh implements RssiFunction {
    final double a ,b, k, bias;

    public Atanh(double a, double b, double k, double bias) {
        this.a = a;
        this.b = b;
        this.k = -k;
        this.bias = bias;
    }

    public Atanh() {
        this(90.0, 80.0, 20.0, 61.0);
    }

    @Override
    public double of(double rssi) {
        return b * atanh((rssi + bias) / k) + a;
    }

    public double atanh(double x) {
        return Math.log((1.0d + x) / (1.0d - x));
    }
}
