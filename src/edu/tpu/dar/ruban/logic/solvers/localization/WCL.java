package edu.tpu.dar.ruban.logic.solvers.localization;

import edu.tpu.dar.ruban.logic.solvers.Dataset;
import edu.tpu.dar.ruban.logic.solvers.Estimator;
import edu.tpu.dar.ruban.logic.solvers.solution.Dimensions;

public class WCL implements Estimator {
    final double a;

    public WCL(double a) {
        this.a = -a;    // as a is used only with a minus
    }

    @Override
    public Dimensions estimate(Dataset[] dataset) {
        double rho_a;
        double numeratorX = 0.0, numeratorY = 0.0, denominator = 0.0;

        for (Dataset d : dataset) {
            rho_a = Math.pow(d.rho, a);
            numeratorX += rho_a * d.sx;
            numeratorY += rho_a * d.sy;
            denominator += rho_a;
        }

        return new Dimensions(numeratorX/denominator, numeratorY/denominator, 0);
    }
}
