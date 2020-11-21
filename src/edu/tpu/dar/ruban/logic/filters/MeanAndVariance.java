package edu.tpu.dar.ruban.logic.filters;

import edu.tpu.dar.ruban.utils.ArrayListDouble;
import edu.tpu.dar.ruban.utils.ArrayListInt;
import edu.tpu.dar.ruban.utils.Pair;

import java.util.Arrays;

public class MeanAndVariance implements Filter<Pair.DoubleDouble> {
    ArrayListInt rssis = new ArrayListInt(40);
    long rssiSum = 0;
    long size = 0;

    @Override
    public void put(int rssi) {
        rssiSum += rssi;
        size++;
    }

    @Override
    public Pair.DoubleDouble calculate() {
        Pair.DoubleDouble mv = getMeanAndVariance();
        clear();
        return mv;
    }

    private void clear() {
        rssiSum = 0;
        size = 0;
        rssis.clear();
    }

    public Pair.DoubleDouble getMeanAndVariance() {
        double mean = ((double) rssiSum) / size;

        double variance = 0.0;
        for (int i = 0; i < rssis.size(); i++) {
            variance += Math.pow(rssis.get(i) - mean, 2);
        }
        variance /= size;

        return new Pair.DoubleDouble(mean, variance);
    }
}
