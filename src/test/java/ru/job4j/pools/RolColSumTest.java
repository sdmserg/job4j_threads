package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

class RolColSumTest {
    @Test
    public void whenAsyncSumMatrix3ThenSuccess() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);
        RolColSum.Sums[] expected = {
                new RolColSum.Sums(6, 12),
                new RolColSum.Sums(15, 15),
                new RolColSum.Sums(24, 18)
        };
        assertThat(result).containsExactly(expected);
    }

    @Test
    public void whenSyncSumMatrix3ThenSuccess() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        RolColSum.Sums[] expected = {
                new RolColSum.Sums(6, 12),
                new RolColSum.Sums(15, 15),
                new RolColSum.Sums(24, 18)
        };
        assertThat(result).containsExactly(expected);
    }

    @Test
    public void whenAsyncSumMatrix5ThenSuccess() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {7, 8, 9, 2, 9},
                {0, 3, 6, 2, 5},
                {1, 7, 5, 8, 2}
        };
        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);
        RolColSum.Sums[] expected = {
                new RolColSum.Sums(15, 15),  // row 0
                new RolColSum.Sums(40, 27),  // row 1
                new RolColSum.Sums(35, 31),  // row 2
                new RolColSum.Sums(16, 25),  // row 3
                new RolColSum.Sums(23, 31)   // row 4
        };
        assertThat(result).containsExactly(expected);
    }

    @Test
    public void whenSyncSumMatrix5ThenSuccess() {
        int[][] matrix = {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {7, 8, 9, 2, 9},
                {0, 3, 6, 2, 5},
                {1, 7, 5, 8, 2}
        };
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        RolColSum.Sums[] expected = {
                new RolColSum.Sums(15, 15),  // row 0
                new RolColSum.Sums(40, 27),  // row 1
                new RolColSum.Sums(35, 31),  // row 2
                new RolColSum.Sums(16, 25),  // row 3
                new RolColSum.Sums(23, 31)   // row 4
        };
        assertThat(result).containsExactly(expected);
    }
}