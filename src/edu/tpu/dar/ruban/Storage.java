package edu.tpu.dar.ruban;

import java.util.ArrayList;
import java.util.HashMap;

public class Storage {
    HashMap<String, ArrayList<MeasurementMoment>> measurements;

    public Storage() {
        measurements = new HashMap<>(3);
    }

    public void add(String mac, MeasurementMoment mm) {
        ArrayList<MeasurementMoment> mma = measurements.computeIfAbsent(mac, k -> new ArrayList<>(50));
        mma.add(mm);
    }

}
