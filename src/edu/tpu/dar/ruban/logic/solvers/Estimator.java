package edu.tpu.dar.ruban.logic.solvers;

import edu.tpu.dar.ruban.logic.measurement.Measurement;

public interface Estimator {
    Dimensions estimate(Measurement[] dataset);
}
