package edu.tpu.dar.ruban.logic.filters;

import edu.tpu.dar.ruban.utils.ArrayListInt;

public class Hampel implements Filter<int[]> {
    ArrayListInt rssis = new ArrayListInt(40);
    final double n;
    final double k = 1.4826;    // for Gaussian

    public Hampel(double sigmasNumber) {
        this.n = sigmasNumber;
    }

    public Hampel() {
        this(3);
    }

    @Override
    public void put(int rssi) {
        rssis.add(rssi);
    }

    @Override
    public int[] calculate() {
        int[] rssiArray = rssis.toArray();  // After calculations use it for return
        int L = rssis.size();
        double median = Median.median(rssiArray);

        int[] rssi_median = new int[rssiArray.length];
        for (int i = 0; i < L; i++) {
            rssi_median[i] = (int) Math.abs(rssis.get(i) - median);
        }
        double MAD = Median.median(rssi_median);

        double S = n * k * MAD;

        int medianInt = (int)Math.round(median);
        for (int i = 0; i < L; i++) {
            int z = rssis.get(i);
            rssiArray[i] = (Math.abs(z-medianInt) > S) ? medianInt : z;
        }

        rssis.clear();
        return rssiArray;
    }
}
