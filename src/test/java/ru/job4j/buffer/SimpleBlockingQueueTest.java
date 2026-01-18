package ru.job4j.buffer;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleBlockingQueueTest {

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(
                () -> {
                    IntStream.range(0, 5).forEach(i -> {
                        try {
                            queue.offer(i);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
    }

    @Test
    public void whenProducerAdd4ElementsQueueIsFullAndConsumerRead3Elements() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        for (int i = 1; i < 5; i++) {
                            try {
                                queue.offer(i);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
        );
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        producer.start();
        Thread.sleep(1000);
        producer.interrupt();
        producer.join();
        consumer.start();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).hasSize(3).containsExactly(1, 2, 3);
    }

    @Test
    public void whenConsumerStartBeforeProducerThenConsumerWaitProducer() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(
                () -> {
                    try {
                        queue.offer(1);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
        );
        Thread consumer = new Thread(
                () -> {
                    try {
                        buffer.add(queue.poll());
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
        );
        consumer.start();
        Thread.sleep(2000);
        producer.start();
        producer.join();
        consumer.join();
        assertThat(buffer).hasSize(1).containsExactly(1);
    }
}