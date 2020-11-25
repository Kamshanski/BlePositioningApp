package edu.tpu.dar.ruban.logic.measurement;

import edu.tpu.dar.ruban.logic.core.Beacon;
import edu.tpu.dar.ruban.logic.core.MacAddress;
import edu.tpu.dar.ruban.logic.distancefunctions.RssiFunction;
import edu.tpu.dar.ruban.logic.filters.Filter;
import edu.tpu.dar.ruban.logic.solvers.Estimator;
import edu.tpu.dar.ruban.logic.solvers.Dimensions;
import edu.tpu.dar.ruban.utils.U;

import java.util.ArrayList;
import java.util.LinkedList;

public class Moment {
    MacAddress macAddress;
    long timeStart, timeEnd;    // timestamp
    LinkedList<Measurement> measurements = new LinkedList<>();
    Dimensions q;               // estimation
    Metadata metadata;          // function, estimator and filters simple class names

    public Moment(long timeStart, long timeEnd, MacAddress macAddress) {
        this.macAddress = macAddress;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public void addMeasurement(int rssi, Beacon beacon) {
        measurements.addFirst(new Measurement(rssi, beacon));
    }

    public void setMetadata(Filter<?> filter, RssiFunction function, Estimator estimation) {
        metadata = new Metadata(filter, function, estimation);
    }

    public void setEstimation(Dimensions q) {
        this.q = q;
    }

    public Measurement[] getMeasurements() {
        Measurement[] m = measurements.toArray(new Measurement[0]);
        return m;
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }
}
