package edu.tpu.dar.ruban.logic.measurement;

import edu.tpu.dar.ruban.logic.distancefunctions.RssiFunction;
import edu.tpu.dar.ruban.logic.filters.Filter;
import edu.tpu.dar.ruban.logic.solvers.Estimator;

public class Metadata {
    final String filter;
    final String function;
    final String estimation;

    public Metadata(Filter<?> filter, RssiFunction function, Estimator estimation) {
        this.filter = filter.getClass().getSimpleName();
        this.function = function.getClass().getSimpleName();
        this.estimation = estimation.getClass().getSimpleName();
    }

}
