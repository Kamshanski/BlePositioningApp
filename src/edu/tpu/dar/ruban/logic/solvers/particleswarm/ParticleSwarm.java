package edu.tpu.dar.ruban.logic.solvers.particleswarm;

import edu.tpu.dar.ruban.logic.solvers.Dataset;
import edu.tpu.dar.ruban.logic.solvers.Estimator;
import edu.tpu.dar.ruban.logic.solvers.solution.Dimensions;

import java.util.Random;

public class ParticleSwarm implements Estimator {
    private double gx, gy, gf, gxTemp, gyTemp, gfTemp;      // global best g=q(x,y) and f(q)
    int gid, gidTemp;               // Id of best and tempBest point
    private int N;                  // cycles limit, maxIterations
    int xN, yN, pNum;               // points along x and y axis (then total is xN*yN points)
    double xMax, xMin, yMax, yMin;  // constraints to search inside
    Point2D[] points;               //
    Function f;                     // function J(q_hat) to minimize
    Random uniformDistribution;

    /**
     *
     * @param maxIterations - N - cycles limit
     * @param pointsGrid - [xN, yN] - points along x and y axis (then total is xN*yN points)
     * @param constraints - [xMin, xMax, yMin, yMax]
     */
    public ParticleSwarm(int maxIterations, int[] pointsGrid, double[] constraints) {
        this.N = maxIterations;
        xMin = constraints[0];                      // init constraints
        xMax = constraints[1];                      //
        yMin = constraints[2];                      //
        yMax = constraints[3];                      //

        uniformDistribution = new Random();         // set only ONE uniformly distributed random digits source for estimator

        xN = pointsGrid[0];                         // Points on Ox axe
        yN = pointsGrid[1];                         // --------- Oy ---
        pNum = xN*yN;                               // --------- Oy ---
        points = new Point2D[pNum];                 //

        createShuffledPoints();

        gxTemp = gx;                                // Best point staring values
        gyTemp = gy;                                //
        gfTemp = gf;                                //
        gidTemp = gid;                              //
    }

    public void createShuffledPoints() {
        double dx = (xMax - xMin) / (xN - 1);
        double dy = (yMax - yMin) / (yN - 1);

        double[] xGrid = new double[xN];
        double[] yGrid = new double[yN];

        for (int i = 0; i < xN; i++) {
            xGrid[i] = xMin + dx*(double) (i);
        }
        for (int i = 0; i < yN; i++) {
            yGrid[i] = yMin + dy*(double) (i);
        }

        int id = 0;
        for (int i = 0; i < xN; i++) {
            double x = xGrid[i];
            for (int j = 0; j < yN; j++) {
                double y = yGrid[j];

                Point2D p = new Point2D(x, y, uniformDistribution);
                double fv = f.of(x, y);
                p.setFunctionValue(fv);
                p.setId(id);

                // Check for best or just the first point created
                if (fv < gf || id == 0) {
                    gx = x;
                    gy = y;
                    gf = fv;
                    gid = id;
                }

                points[id] = p;
                id++;
            }
        }
    }


    @Override
    public Dimensions estimate(Dataset[] dataset) {
        f = new LeastSquaresFunction(dataset);  // update function
        double fv;

        for (int i = 0; i < N; i++)
        {   // Step
            for (int j = 0; j < pNum; j++) {
                Point2D p = points[i];

                p.updatePosition(gx, gy);
                fv = f.of(p.x, p.y);
                p.setFunctionValue(fv);

                if (fv < gf) {  // Check if found new best point
                    gfTemp = fv;
                    gxTemp = p.x;
                    gyTemp = p.y;
                    gidTemp = p.id;
                }
            }

            if (gfTemp < gf) {  // Update best
                gf = gfTemp;
                gx = gxTemp;
                gy = gyTemp;
                gid = gidTemp;
            }
        }

        Dimensions output = new Dimensions(gx, gy,0);
        output.fv = gf;

        return null;
    }
}
