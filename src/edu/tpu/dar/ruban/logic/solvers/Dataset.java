package edu.tpu.dar.ruban.logic.solvers;


public class Dataset {
    public final double rho, rssi;
    public final double sx, sy;

    public Dataset(double rho, double sx, double sy, double rssi) {
        this.rho = rho;
        this.rssi = rssi;
        this.sx = sx;
        this.sy = sy;
    }
}