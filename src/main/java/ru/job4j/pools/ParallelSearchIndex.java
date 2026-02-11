package ru.job4j.pools;

import java.util.concurrent.RecursiveTask;
import java.util.Objects;
import java.util.Optional;

public class ParallelSearchIndex<T> extends RecursiveTask<Optional<Integer>> {
    private final T[] array;
    private final int from;
    private final int to;
    private final T value;

    public ParallelSearchIndex(T[] array, int from, int to, T value) {
        this.array = array;
        this .from = from;
        this.to = to;
        this.value = value;
    }

    @Override
    protected Optional<Integer> compute() {
        if ((to - from) <= 10) {
            return linearSearch();
        }
        int middle = (from + to) / 2;
        var leftArray = new ParallelSearchIndex<>(array, from, middle, value);
        var rightArray = new ParallelSearchIndex<>(array, middle + 1, to, value);
        leftArray.fork();
        rightArray.fork();
        Optional<Integer> leftIndex = leftArray.join();
        Optional<Integer> rightIndex = rightArray.join();
        return leftIndex.or(() -> rightIndex);
     }

    private Optional<Integer> linearSearch() {
        for (int i = from; i <= to; i++) {
            if (Objects.equals(array[i], value)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
}
