package edu.tpu.dar.ruban.utils;

public class Pair {

    public static class IntDouble {
        public int first;
        public double second;

        public IntDouble(int first, double second) {
            this.first = 0;
            this.second = 0;
        }

        public void add(int dFirst, double dSecond) {
            first += dFirst;
            second += dSecond;
        }
    }

    public static class IntInt {
        public int first;
        public int second;

        public IntInt(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
}
