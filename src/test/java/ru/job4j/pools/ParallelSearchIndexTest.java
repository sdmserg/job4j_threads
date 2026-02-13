package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelSearchIndexTest {

    @Test
    public void whenRecursiveSearchIntegerArrayThenSuccess() {
        Integer[] array = {
                12, 7, 25, 3, 18, 30, 5, 14,
                9, 21, 11, 2, 28, 16, 1, 20,
                6, 13, 27, 8, 24, 4, 19, 22,
                10, 15, 23, 26, 17, 29, 0, 31
        };
        int value = 0;
        Optional<Integer> result = ParallelSearchIndex.parallelSearch(array, value);
        int expectedIndex = 30;
        assertThat(result.get()).isEqualTo(expectedIndex);
    }

    @Test
    public void whenRecursiveSearchStringArrayThenSuccess() {
        String[] array = {
                    "Ivan", "Alexey", "Maria", "Olga", "Dmitry",
                    "Tatiana", "Sergey", "Elena", "Nikolay", "Anna",
                    "Vladimir", "Ekaterina", "Pavel", "Svetlana", "Yulia",
                    "Andrey", "Oksana", "Igor", "Natalia", "Mikhail"
        };
        String value = "Anna";
        Optional<Integer> result = ParallelSearchIndex.parallelSearch(array, value);
        int expectedIndex = 9;
        assertThat(result.get()).isEqualTo(expectedIndex);
    }

    @Test
    public void whenLinearSearchIntegerArrayThenSuccess() {
        Integer[] array = {
                12, 7, 25, 3, 18, 30, 5, 14,
        };
        int value = 30;
        Optional<Integer> result = ParallelSearchIndex.parallelSearch(array, value);
        int expectedIndex = 5;
        assertThat(result.get()).isEqualTo(expectedIndex);
    }

    @Test
    public void whenLinearSearchStringArrayThenSuccess() {
        String[] array = {
                "Ivan", "Alexey", "Maria", "Olga", "Dmitry"
        };
        String value = "Olga";
        Optional<Integer> result = ParallelSearchIndex.parallelSearch(array, value);
        int expectedIndex = 3;
        assertThat(result.get()).isEqualTo(expectedIndex);
    }

    @Test
    public void whenIntegerNotInArrayThenReturnEmptyOptional() {
        Integer[] array = {
                12, 7, 25, 3, 18, 30, 5, 14,
                9, 21, 11, 2, 28, 16, 1, 20,
                6, 13, 27, 8, 24, 4, 19, 22,
                10, 15, 23, 26, 17, 29, 0, 31
        };
        int value = 1000;
        Optional<Integer> result = ParallelSearchIndex.parallelSearch(array, value);
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
        Optional<Integer> result = ParallelSearchIndex.parallelSearch(array, value);
        assertThat(result.isEmpty()).isTrue();
    }
}