package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    public static class Sums {
        private final int rowSum;
        private final int colSum;

        public Sums(final int rowSum, final int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || o.getClass() != getClass()) {
                return false;
            }
            Sums other = (Sums) o;
            return other.rowSum == rowSum
                    && other.colSum == colSum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(rowSum, colSum);
        }
    }

    public static Sums[] sum(int[][] matrix) {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        for (int row = 0; row < n; row++) {
            int rowSum = 0;
            int colSum = 0;
            for (int col = 0; col < n; col++) {
                rowSum += matrix[row][col];
                colSum += matrix[col][row];
            }
            sums[row] = new Sums(rowSum, colSum);
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        for (int index = 0; index < n; index++) {
            futures.put(index, getAsyncTask(matrix, index));
        }
        for (Integer index: futures.keySet()) {
            sums[index] = futures.get(index).get();
        }
        return sums;
    }

    private static CompletableFuture<Sums> getAsyncTask(int[][] matrix, int index) {
        return CompletableFuture.supplyAsync(
                () -> {
                    int n = matrix.length;
                    int rowSum = 0;
                    int colSum = 0;
                    for (int row = index; row <= index; row++) {
                        for (int col = 0; col < n; col++) {
                            rowSum += matrix[index][col];
                            colSum += matrix[col][index];
                        }
                    }
                    return new Sums(rowSum, colSum);
                }
        );
    }
}
