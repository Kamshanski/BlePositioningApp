package edu.tpu.dar.ruban.logic.solvers.localization;

import edu.tpu.dar.ruban.logic.solvers.Dataset;
import edu.tpu.dar.ruban.logic.solvers.Estimator;
import edu.tpu.dar.ruban.logic.solvers.solution.Dimensions;

public class WCL implements Estimator {
    final double a;
    Dataset dataset;

    public WCL(double a, Dataset dataset) {
        this.a = a;
        this.dataset = dataset;
    }

    @Override
    public Dimensions estimate(Dataset dataset) {
        return null;
    }
}
