package edu.tpu.dar.ruban.logic.solvers.particleswarm;

import edu.tpu.dar.ruban.logic.solvers.Dataset;

public class LeastSquaresFunction implements Function {

    private Dataset[] dataset;
    public LeastSquaresFunction(Dataset[] dataset) {
        this.dataset = dataset;
    }

    public void setDataset(Dataset[] dataset) {
        this.dataset = dataset;
    }

    @Override
    public double of(double x, double y) {
        double sum = 0.0;
        double S;

        // sum( ( rho - sqrt((x - sx)^2 + (y - sy)^2) )^2 ) =
        // = sum(rho^2 + (x - sx)^2 + (y - sy)^2 - 2*rho*sqrt((x - sx)^2 + (y - sy)^2))) =
        // = sum(rho^2 + S - 2*rho*sqrt(S)))
        // S = (x - sx)^2 + (y - sy)^2
        for (Dataset d : dataset) {
            S = Math.pow((x - d.sx), 2) + Math.pow((y - d.sy), 2);
            sum += Math.pow(Math.pow(d.rho, 2) + S - 2*d.rho*Math.sqrt(S), 2);
        }

        return sum;
    }




}
