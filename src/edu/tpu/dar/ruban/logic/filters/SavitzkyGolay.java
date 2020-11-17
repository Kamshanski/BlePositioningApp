package edu.tpu.dar.ruban.logic.filters;

import edu.tpu.dar.ruban.utils.ArrayListInt;
import edu.tpu.dar.ruban.utils.SlidingWindowIntArray;     
import edu.tpu.dar.ruban.utils.U;
import org.jblas.DoubleMatrix;
import org.jblas.Solve;

// Ну как бы есть Но для позиционирования он не понадобится( Мог и не делать Т_Т. Даже лишьние библиотеки подклучать не пришлось бы ТТТТ_ТТТТ(
public class SavitzkyGolay implements Filter<Double> {
    public static final int QUADRATIC = 2;
    public static final int CUBIC = 3;
    final int N, degree, L, M;
    SlidingWindowIntArray windowIntArray;
    double[] A_T_0;

    public SavitzkyGolay(int windowSize, int degree) {
        if ((windowSize & 1) != 1) {
            windowSize++;   // Only odd windowSize
        }
        this.N = windowSize;
        this.degree = degree;
        windowIntArray = new SlidingWindowIntArray(N);
        this.M = degree+1;
        this.L = (N - 1) >>> 1;    // (N-1)/2
        DoubleMatrix A = new DoubleMatrix(N, M);
        int[] indices = new int[2];
        int l, lpow = 1;
        for (int i = 0; i < N; i++) {
            indices[0] = i;
            lpow = 1;
            l = -L + i;
            for (int j = 0; j < M; j++) {
                indices[1] = j;
                A.put(indices, lpow);
                lpow *= -l;
            }
        }
        // Save computed coefs
        DoubleMatrix coefs = Solve.pinv(A).getRow(0);
        A_T_0 = new double[M];
        for (int i = 0; i < N; i++) {
            A_T_0[i] = coefs.get(0, i);
        }

        U.nout(coefs.toString());
    }

    @Override
    public void put(int rssi) {
        windowIntArray.put(rssi);
    }

    @Override
    public Double calculate() {
        double result = 0.0;
        for (int i = 0; i < N; i++) {
            result += windowIntArray.get(i)*A_T_0[i];
        }
        return result;
    }
}
