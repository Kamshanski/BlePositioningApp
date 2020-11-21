package edu.tpu.dar.ruban.logic;

import edu.tpu.dar.ruban.logic.core.MacAddress;
import edu.tpu.dar.ruban.logic.distancefunctions.Atanh;
import edu.tpu.dar.ruban.logic.distancefunctions.RssiFunction;
import edu.tpu.dar.ruban.logic.filters.Filter;
import edu.tpu.dar.ruban.logic.filters.Kalman;
import edu.tpu.dar.ruban.logic.measurement.Metadata;
import edu.tpu.dar.ruban.logic.solvers.Estimator;
import edu.tpu.dar.ruban.logic.solvers.particleswarm.ParticleSwarm;

import java.util.HashMap;

public class Positioning {
    Estimator estimator;
    RssiFunction rssiConverter;     // Хорошо было бы отдать функцию маячкам, если были бы измерения для каждого маячка
    HashMap<MacAddress, Filter<?>> filters;
    Kalman filter;
    Metadata metadata;

    public Positioning() {
        estimator = new ParticleSwarm(60, new int[] {7,7}, new double[] {-20, 240, -20, 220});
        rssiConverter = new Atanh(90.0, 80.0, 20.0, 61.0);
        filter = new Kalman(1.0, 1.0);
    }

    public double computeDistance(int rssi) {
        return rssiConverter.of(rssi);
    }

    public Filter<?> getFilter(MacAddress mac) {
        return filters.computeIfAbsent(mac, k -> new Kalman(1.0, 1.0));
    }

    Metadata getMetadata() {
        if (metadata == null) {
            metadata = new Metadata(filter, rssiConverter, estimator);
        }
        return metadata;
    }
}
