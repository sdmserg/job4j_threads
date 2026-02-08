package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelSearchIndexTest {

    @Test
    public void whenRecursiveSearchIntegerArrayThenSuccess() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Integer[] array = {
                12, 7, 25, 3, 18, 30, 5, 14,
                9, 21, 11, 2, 28, 16, 1, 20,
                6, 13, 27, 8, 24, 4, 19, 22,
                10, 15, 23, 26, 17, 29, 0, 31
        };
        int value = 0;
        int expectedIndex = 30;
        Optional<Integer> result = forkJoinPool.invoke(
                new ParallelSearchIndex<Integer>(array, 0, array.length - 1, value)
        );
        assertThat(result.get()).isEqualTo(expectedIndex);
    }

    @Test
    public void whenRecursiveSearchStringArrayThenSuccess() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        String[] array = {
                    "Ivan", "Alexey", "Maria", "Olga", "Dmitry",
                    "Tatiana", "Sergey", "Elena", "Nikolay", "Anna",
                    "Vladimir", "Ekaterina", "Pavel", "Svetlana", "Yulia",
                    "Andrey", "Oksana", "Igor", "Natalia", "Mikhail"
        };
        String value = "Anna";
        int expectedIndex = 9;
        Optional<Integer> result = forkJoinPool.invoke(
                new ParallelSearchIndex<String>(array, 0, array.length - 1, value)
        );
        assertThat(result.get()).isEqualTo(expectedIndex);
    }

    @Test
    public void whenLinearSearchIntegerArrayThenSuccess() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Integer[] array = {
                12, 7, 25, 3, 18, 30, 5, 14,
        };
        int value = 30;
        int expectedIndex = 5;
        Optional<Integer> result = forkJoinPool.invoke(
                new ParallelSearchIndex<Integer>(array, 0, array.length - 1, value)
        );
        assertThat(result.get()).isEqualTo(expectedIndex);
    }

    @Test
    public void whenLinearSearchStringArrayThenSuccess() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        String[] array = {
                "Ivan", "Alexey", "Maria", "Olga", "Dmitry"
        };
        String value = "Olga";
        int expectedIndex = 3;
        Optional<Integer> result = forkJoinPool.invoke(
                new ParallelSearchIndex<String>(array, 0, array.length - 1, value)
        );
        assertThat(result.get()).isEqualTo(expectedIndex);
    }

    @Test
    public void whenIntegerNotInArrayThenReturnEmptyOptional() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Integer[] array = {
                12, 7, 25, 3, 18, 30, 5, 14,
                9, 21, 11, 2, 28, 16, 1, 20,
                6, 13, 27, 8, 24, 4, 19, 22,
                10, 15, 23, 26, 17, 29, 0, 31
        };
        int value = 1000;
        Optional<Integer> result = forkJoinPool.invoke(
                new ParallelSearchIndex<Integer>(array, 0, array.length - 1, value)
        );
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void whenStringNotInArrayThenReturnEmptyOptional() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        String[] array = {
                "Ivan", "Alexey", "Maria", "Olga", "Dmitry",
                "Tatiana", "Sergey", "Elena", "Nikolay", "Anna",
                "Vladimir", "Ekaterina", "Pavel", "Svetlana", "Yulia",
                "Andrey", "Oksana", "Igor", "Natalia", "Mikhail"
        };
        String value = "Roman";
        Optional<Integer> result = forkJoinPool.invoke(
                new ParallelSearchIndex<String>(array, 0, array.length - 1, value)
        );
        assertThat(result.isEmpty()).isTrue();
    }
}