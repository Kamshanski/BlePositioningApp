package edu.tpu.dar.ruban.logic.measurement;

import edu.tpu.dar.ruban.logic.core.Beacon;

public class Measurement {
    public final int rssi;         // rssi measured by beacon
    public double rho;             // distance (computed by RSSI-to-distance function)
    public final Beacon beacon;    // to get beacon coordinates [sx, sy] and save memory

    public Measurement(int rssi, Beacon beacon) {
        this.rssi = rssi;
        this.beacon = beacon;
    }

    public void setDistance(double rho) {
        this.rho = rho;
    }

    public double sx() {
        return beacon.x;
    }

    public double sy() {
        return beacon.y;
    }
}
