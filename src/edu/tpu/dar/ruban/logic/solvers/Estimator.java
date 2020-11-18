package edu.tpu.dar.ruban.logic.solvers;

import edu.tpu.dar.ruban.logic.solvers.solution.Dimensions;

public interface Estimator {
    Dimensions estimate(Dataset[] dataset);
}
