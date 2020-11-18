package edu.tpu.dar.ruban.logic.solvers.particleswarm;

import edu.tpu.dar.ruban.logic.solvers.solution.Dimensions;

import java.util.Random;

// Добавить макс и мин значения для коэффициентов

/**
 * Class for a point of swamp. It comprises coordinates, velocity, random behavior coefficients and local memory.
 */
public class Point2D {
    int id;
    double x, y;
    double xVel, yVel;
    double xr, yr, fr;          // Best local r=q(x, y) and f(r)
    double fv;
    double Cin;                 // Main coefficients, that defines algorithm behavior
    double Ccog;                //
    double Csoc;                //

    public Point2D(double x, double y, Random U) {
        this.x = x;
        this.y = y;
        xr = x;
        yr = y;
        xVel = U.nextDouble();
        yVel = U.nextDouble();
        Cin = U.nextDouble();
        Ccog = U.nextDouble();
        Csoc = U.nextDouble();
    }

    // output - array of x,y,z to fill to save memory
    public void updatePosition(double xGlobalBest, double yGlobalBest) {
        xVel = Cin*xVel + Ccog*(xr - x) + Csoc*(xGlobalBest - x);   // Main formula of the algorithm update
        yVel = Cin*yVel + Ccog*(yr - y) + Csoc*(yGlobalBest - y);

        x = x + xVel;
        y = y + yVel;
    }

    public void setFunctionValue(double fv) {
        this.fv = fv;
        if (fv < fr) {
            fr = fv;
            xr = x;
            yr = y;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
