package edu.tpu.dar.ruban.logic.distancefunctions;


/**
 * pho = exp((RSSId-RSSI)/a*ln(d)) => pho = exp(b + RSSI * a)
 * a = -ln(d)/a
 * b = RSSId*ln(d)/a
 */
public class Exponent implements RssiFunction {
    final double pivotDistance, pivotRssi, a, b;

    public Exponent(double pivotDistance, double pivotRssi, double a) {
        this.pivotDistance = pivotDistance;
        this.pivotRssi = pivotRssi;
        double ln_d = Math.log(pivotDistance);
        // apply simplification
        this.a = - ln_d / a;
        this.b = pivotRssi * ln_d / a;

    }

    @Override
    public double of(double rssi) {
        return Math.exp(a * rssi + b);
    }
}
