package edu.tpu.dar.ruban;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MeasurementMoment {
    private long timeStart, timeEnd;    // Locale time
    private HashMap<Integer, Double> rssiArray;

    public MeasurementMoment(long timeStart, long timeEnd) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        rssiArray = new HashMap<>(5);
    }

    private void put(int id, double rssi) {
        rssiArray.put(id, rssi);
    }

    public static class MeasurementMomentBuilder {
        long timeStart, timeEnd;    // Locale time
        HashMap<Integer, Pair.IntDouble> rssiArray;    // <id, pairOfNumberOfRssisAndSumOfRssis>

        public MeasurementMomentBuilder(long timeStart, long timeEnd) {
            long curTime = System.currentTimeMillis();
            timeEnd = curTime;
            timeStart = curTime + (timeStart - timeEnd);
            this.rssiArray = new HashMap<>(5);
        }

        void put(int id, int rssi) {
            Pair.IntDouble pid = rssiArray.computeIfAbsent(id, k -> new Pair.IntDouble(0, 0.0));
            pid.add(1, rssi);
        }

        MeasurementMoment build() {
            MeasurementMoment mm = new MeasurementMoment(timeStart, timeEnd);

            rssiArray.forEach( (id, numRssiPair) -> {
                mm.put(id, numRssiPair.second/numRssiPair.first);
            });

            return mm;
        }
    }
}
