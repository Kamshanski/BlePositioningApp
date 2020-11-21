package edu.tpu.dar.ruban.logic.solvers.localization;

import edu.tpu.dar.ruban.logic.measurement.Measurement;
import edu.tpu.dar.ruban.logic.solvers.Estimator;
import edu.tpu.dar.ruban.logic.solvers.Dimensions;

public class RSWL implements Estimator {
    final double lambda, base;
    final double RSSI_max;

    public RSWL(double lambda, double RSSI_max) {
        this.lambda = lambda;       // as a is used only with a minus a
        this.base = 1.0-lambda;     //
        this.RSSI_max = RSSI_max;   // Assume thar RSSI_max equals for all beacons
    }

    @Override
    public Dimensions estimate(Measurement[] dataset) {
        double exp;
        double numeratorX = 0.0, numeratorY = 0.0, denominator = 0.0;

        for (Measurement d : dataset) {
            exp = Math.pow(base, RSSI_max - d.rssi);

            numeratorX += exp * d.sx();
            numeratorY += exp * d.sy();
            denominator += exp;
        }

        return new Dimensions(numeratorX/denominator, numeratorY/denominator, 0);
    }
}
