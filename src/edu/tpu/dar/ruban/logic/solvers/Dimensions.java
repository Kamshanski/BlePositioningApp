package edu.tpu.dar.ruban.logic.solvers;

public class Dimensions {
    public double x, y, z;
    public double fv;       // optional

    public Dimensions(double x, double y, double z) {
        set(x, y, z);
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

    }

    public void set(double x, double y) {
        set(x, y, 0);
    }


}
